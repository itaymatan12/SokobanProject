package solver;

import java.util.Set;
import search.BFS;
import search.Position;
import search.Solution;
import strips.AND;
import strips.Action;
import strips.Plannable;
import strips.Predicate;

public class SokobanPlannable {

	static char[][] levelrepresenter;

	public SokobanPlannable(char[][] l) {
		SokobanPlannable.levelrepresenter = l;
	}

	public static AND getGoal(AND kb) {
		AND goal = new AND();

		for (Predicate p : kb.getPredicates()) {
			if (p.getID().startsWith("t")) {

				goal.add(new Predicate("boxAt", "b" + p.getID().substring(1), p.getValue()));
			}
		}
		return goal;
	}

	public static AND getKnowledgeBase() {
		char[][] level = levelrepresenter;
		AND kb = new AND();
		int boxCount = 0;
		int targetCount = 0;
		for (int i = 0; i < levelrepresenter.length; i++) {
			for (int j = 0; j < levelrepresenter[i].length; j++) {
				switch (level[i][j]) {
				case '#':
					kb.add(new Predicate("wallAt", "", i + "," + j));
					break;
				case ' ':
					kb.add(new Predicate("clearAt", "", i + "," + j));
					break;
				case 'A':
					kb.add(new Predicate("sokobanAt", "?", i + "," + j));
					break;
				case '@':
					boxCount++;
					kb.add(new Predicate("boxAt", "b" + boxCount, i + "," + j));
					break;
				case 'o':
					targetCount++;
					kb.add(new Predicate("targetAt", "t" + targetCount, i + "," + j));
					break;
				}
			}
		}
		return kb;
	}

	public static Plannable readLevel() {
		try {

			AND kb = getKnowledgeBase();
			AND goal = getGoal(kb);
			Plannable plannable = new Plannable() {

				@Override
				public Action getsatisfyingAction(Predicate top) {
					Action action = null;

					Position sokoban = null;
					Position target = null;
					Position box = null;

					if (top.getType().startsWith("boxAt")) 
					{
						int rowb = 0; 
						int colb = 0; 
						loop: for (Predicate player : kb.getPredicates()) {

							if (player.getType().startsWith("sokobanAt")) {
								int rowp = Integer
										.parseInt(player.getValue().substring(0, player.getValue().indexOf(',')));
								int colp = Integer.parseInt(player.getValue()
										.substring(player.getValue().indexOf(',') + 1, player.getValue().length()));
								sokoban = new Position(rowp, colp);
								break loop;
							}
						}


						loop: for (Predicate p : kb.getPredicates()) {

							if (p.getType().startsWith("tar")
									&& p.getID().substring(1).equals(top.getID().substring(1))) {

								int rowt = Integer.parseInt(p.getValue().substring(0, p.getValue().indexOf(',')));
								int colt = Integer.parseInt(
										p.getValue().substring(p.getValue().indexOf(',') + 1, p.getValue().length()));
								target = new Position(rowt, colt);
								break loop;
							}
						}

						loop: for (Predicate p : kb.getPredicates()) {

							if (p.getType().startsWith("box") && p.getID().equals(top.getID())) {

								rowb = Integer.parseInt(p.getValue().substring(0, p.getValue().indexOf(',')));
								colb = Integer.parseInt(
										p.getValue().substring(p.getValue().indexOf(',') + 1, p.getValue().length()));
								box = new Position(rowb, colb);
								break loop;
							}
						}

						Move adapter = new Move(levelrepresenter, box, target);
						BFS<StateM> bfs = new BFS<StateM>();
						Solution sol1 = bfs.search(adapter);

						if (sol1 != null) {
							search.Action BFSFirstAction = sol1.getActions().get(0);
							switch (BFSFirstAction.getName()) {
							case "move up":
								rowb += 1;
								break;
							case "move down":
								rowb -= 1;
								break;
							case "move left":
								colb += 1;
								break;
							case "move right":
								colb -= 1;
								break;
							}
							Move adapter2 = new Move(levelrepresenter, sokoban, new Position(rowb, colb));

							BFS<StateM> bfs2 = new BFS<StateM>();
							Solution sol2 = bfs2.search(adapter2);

							if (sol2 != null) {

								action = new Action("push", box.getRow() + "," + box.getCol(), top.getValue());
								action.getPreconditions().add(new Predicate("sokobanAt", "?", rowb + "," + colb));// move
								action.getPreconditions()
										.add(new Predicate("targetAt", "t" + top.getID().substring(1), top.getValue()));// boxnumber=
								action.getEffects().add(new Predicate("clearAt", "", top.getValue()));
								action.getEffects().add(new Predicate("boxAt", top.getID(), top.getValue()));

								int newrow = Integer.parseInt(top.getValue().substring(0, top.getValue().indexOf(",")));
								int newcol = Integer.parseInt(top.getValue().substring(top.getValue().indexOf(",") + 1,
										top.getValue().length()));

								if (sol2.getActions().size() > 0) {
									switch (sol1.getActions().get(sol1.getActions().size() - 1).getName()) {

									case "move up":
										newrow += 1;
										break;
									case "move down":
										newrow -= 1;
										break;
									case "move left":
										newcol += 1;
										break;
									case "move right":
										newcol -= 1;
										break;
									}
								}
								action.getEffects().add(new Predicate("sokobanAt", "?", newrow + "," + newcol));

							} else {
								System.out.println("No Solution");
								System.exit(1);
							}
						} else {
							System.out.println("No Solution");
							System.exit(1);
						}

					} else if (top.getType().startsWith("sokoban")) {
						int rowp = 0;
						int colp = 0;
						int destrow = 0;
						int destcol = 0;

						loop: for (Predicate player : kb.getPredicates()) {
							if (player.getType().startsWith("sokobanAt")) {
								rowp = Integer.parseInt(player.getValue().substring(0, player.getValue().indexOf(',')));
								colp = Integer.parseInt(player.getValue().substring(player.getValue().indexOf(',') + 1,
										player.getValue().length()));
								sokoban = new Position(rowp, colp);
								break loop;
							}
						}
						destrow = Integer.parseInt(top.getValue().substring(0, top.getValue().indexOf(',')));
						destcol = Integer.parseInt(
								top.getValue().substring(top.getValue().indexOf(',') + 1, top.getValue().length()));

						Move SA = new Move(levelrepresenter, sokoban, new Position(destrow, destcol));
						BFS<StateM> bfs = new BFS<StateM>();
						Solution sol2 = bfs.search(SA);
						if (sol2 != null) {
							action = new Action("move", rowp + "," + colp, top.getValue());
							action.getPreconditions().add(new Predicate("clearAt", "", top.getValue()));
							action.getEffects().add(new Predicate("clearAt", "", rowp + "," + colp));
							action.getEffects().add(new Predicate("sokobanAt", "?", top.getValue()));
						}
					}
					return action;
				}

				@Override
				public Set<Action> getsatisfyingActions(Predicate top) {
					return null;
				}

				@Override
				public AND getKnowledgebase() {
					return kb;
				}

				@Override
				public AND getGoal() {
					return goal;
				}
			};

			return plannable;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}