package reversi.controller;

import reversi.model.CellPos;
import reversi.model.CellState;
import reversi.model.GameState;
import reversi.view.GameView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ゲームの状態管理、ゲームの進行を行います。
 *
 */
public class GameController {

	private GameState state = new GameState();

	public void run() {
		System.out.println("help でヘルプメッセージを出力します。");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in) );
		while(true) {
			System.out.print("Reversi>");
			String line = null;

			try {
				line = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// コマンド解釈
			String[] tokens = line.split("\\s+");
			if(tokens.length > 0) {
				// 終了コマンド
				if(tokens[0].equals("exit")) {
					break;
				}

				switch(tokens[0]) {
				case "help":
					commandHelp(tokens);
					break;
				case "board":
				case "b":
					commandBoard(tokens);
					break;
				case "put":
				case "p":
					commandPut(tokens);
					break;
				case "pass":
					commandPass(tokens);
					break;
				case "reset":
					commandReset(tokens);
					break;
				case "":
					break;
				default:
					System.out.println("無効なコマンドです。");
					break;
				}

			}
		}
	}

	private static void commandHelp(String[] tokens) {
		System.out.println("Usage:");
		System.out.println("  help    このメッセージを表示します。");
		System.out.println("  board   盤面の状態を表示します。");
		System.out.println("  put [行] [列]");
		System.out.println("          盤面の指定の位置に石を置きます。");
		System.out.println("          例) put 2 d");
		System.out.println("              2行d列のマスに石を配置する");
		System.out.println("  history 石を置いた履歴を表示します。");
		System.out.println("  pass    パスします。(次の人に手を譲る)");
		System.out.println("  reset   盤面を初期化します。");
	}

	private void commandBoard(String[] tokens) {
		GameView.showState(state);
	}

    private void commandPut(String[] tokens) {
		if(tokens.length < 3) {
			System.out.println("Invalid argument count.");
			return;
		}

        int arg1;
        try {
            arg1 = Integer.parseInt(tokens[1]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid argument: " + tokens[1]);
            System.out.println("数値を入力してください。");
            return;
        }

        Pattern p = Pattern.compile("[a-h]");
        Matcher m = p.matcher(tokens[2]);
        if(!m.matches()) {
            System.out.println("Invalid argument: " + tokens[2]);
            System.out.println("a～hの文字を入力してください。");
            return;
        }

        char ch = tokens[2].charAt(0);
        int arg2 = ch - 'a' + 1;
        putStone(arg1, arg2);
	}

	private void putStone(int row, int col) {
		CellState stone = GameState.turnToState(state.getTurn());

		// 盤面の範囲内か確認
		if(!state.getBoard().isInRange(row, col)) {
			System.out.println("盤面の範囲外です。");
			return;
		}

		// 既に置いてあるか確認
		if(!(state.getBoard().getCell(row, col) == CellState.BLANK)) {
			System.out.println("既に石が置かれています。");
			return;
		}

		// 反転できるか探索する
		// 全8方向について、異なる色の石がある間、この石をカウントしながら進み、次の条件
		// ・盤の範囲外となる(OuterCell)
		// ・空のマスとなる(BlankCell)
		// ・同じ色の石となる(DarkDisk|LightDisk)
		// に合致すればループを終了する。
		// 同じ色の石が見つかったときのみ、異なる色の石を反転させる。
		List<CellPos> flipCells = state.getBoard().selectFlippableCells(new CellPos(row, col), stone);

		// 反転する石があるか？
		if(flipCells.size() == 0) {
			// 反転する石がない
			System.out.println("反転できる石がありません。");
			return;
		}

        // 反転する石がある

        // 石を置いて
        state.getBoard().setCell(row, col, stone);

        // ひっくり返す
        state.getBoard().flip(flipCells);

        // 手を記録する
        state.registerMove(row, col, flipCells);

        // ゲーム終了か調べる
        if(state.getBoard().hasBlankCell()) {
            // BlankCellがあればゲーム継続
            state.switchTurn();
            GameView.showState(state);
        } else {
            // BlankCellがなければゲーム終了
            System.out.println((state.getBoard().countDarkDisks() > 32 ? "黒" : "白")
                    + "の勝ち");
            state.setGameover(true);
            GameView.showState(state);
        }
	}

	private void commandPass(String[] tokens) {
		state.switchTurn();
		GameView.showState(state);
	}

	private void commandReset(String[] tokens) {
		state.initialize();
	}

}
