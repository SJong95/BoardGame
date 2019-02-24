package boardGame;

import java.util.*;

public class BoardExample {
	static Scanner scan = new Scanner(System.in);
	static boolean run2 = true;

	// 다른 클래스에서도 사용하기 위해 static으로 필드 생성
	public static void main(String[] args) {

		while (run2) {
			Board board = new Horse();
			boolean run = true;
			while (run) {
				board.phan(); // 현재 말 위치 보여줌
				board.hanAtt();// 한나라의 공격 턴
				board.changeXY();// 선택한 말을 이동하는 메소드
				run = board.GameEnd();// 결과 체크
				if (!run) {
					break; // 게임을 다시 할려고 할때 실행됨
				}
				board.phan(); // 현재 말 위치 보여줌
				board.choAtt();// 초나라의 공격 턴
				board.changeXY();// 선택한 말을 이동하는 메소드
				run = board.GameEnd();// 결과 체크
				if (!run) {
					break; // 게임을 다시 할려고 할때 실행됨
				}
			}
		}
	}
}
class Board {
	int[][] board; // 게임판 배열
	String[][] strboard;// 말 위치 표시하기 위한 배열
	int hx = 0; // 한나라의 말이 움직일 x좌표 필드
	int hy = 0; // 한나라의 말이 움직일 y좌표 필드
	int cx = 0; // 초나라의 말이 움직일 x좌표 필드
	int cy = 0; // 초나라의 말이 움직일 y좌표 필드
	int sum = 0;// 어느 나라의 어느 말이 잡힌건지 체크하기 위한 필드
	int oldsum = 36;// sum과 동일

	Board() {
		board = new int[4][4]; // 공간 생성
		strboard = new String[4][4]; // 게임판과 동일 크기

		for (int i = 0; i < board[0].length; i++) {
			board[0][i] = i + 1;
		} // 한나라 말 생성
		for (int i = 0; i < board[0].length; i++) {
			board[3][i] = i + 5;
		} // 초나라 말 생성
	}

	public void phan() { // 현재 게임판의 말 현황을 보여줌
		// 나라 구분 없이 말을 보여줌
		// board의 값에 따라 strboard에 저장하여 표시
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] == 1 || board[i][j] == 5) {
					strboard[i][j] = "P1";
				} else if (board[i][j] == 2 || board[i][j] == 6) {
					strboard[i][j] = "P2";
				} else if (board[i][j] == 3 || board[i][j] == 7) {
					strboard[i][j] = "Q1";
				} else if (board[i][j] == 4 || board[i][j] == 8) {
					strboard[i][j] = "Q2";
				} else {
					strboard[i][j] = "　 "; // 없으면 공백
				}
			}
		}
		for (int i = 0; i < strboard.length; i++) {
			System.out.println(Arrays.toString(strboard[i]));
		} // 출력하는 곳
	}

	public void getList() {

	}

	public void hanAtt() {
	}

	public void choAtt() {
	}

	public boolean GameEnd() {
		return true;
	}

	public void changeXY() {
	}
	// 나머지 모두 추상 메소드로 상속받은 하위 클래스에서 사용하기 위함
}
class Horse extends Board {
	int han = 1; // 한나라 말 이동 후 불필요한 확인을 하지않기 위한 변수
	int cho = 4; // 초나라 말 이동 후 불필요한 확인을 하지않기 위한 변수
	int temp = 0; // 말 이동시 사용
	String hancho = null; // 말 이동하는 메소드에서 어느 나라인지 확인할 때 사용
	String changeB = null; // 어느 말인지 선택할 때 사용
	ArrayList<String> eqlist = new ArrayList<>(); // 이동 가능 위치 저장 리스트
	ArrayList<String> list = new ArrayList<>(); // 어느 나라의 승리인지 확인하기 위한 리스트

	@Override
	public void changeXY() { // 말 이동 메소드
		// 선택된 말에 따라서 이동함
		while (true) {
			if (hancho.equals("han")) { // 한나라 차례일 때
				String xy; // 이동할 위치를 입력 받아 저장하는 변수
				while (true) { // 형식에 맞을 때 까지 입력하기 위함
					System.out.print("\n이동할 위치(x,y): ");
					xy = BoardExample.scan.next();
					if (xy.substring(1, 2).equals(",")) {
						break;
					} else {
						System.out.println("형식에 맞춰서 입력해주세요.");
					}
				}
				int x = Integer.parseInt(xy.substring(0, 1));
				int y = Integer.parseInt(xy.substring(2));
				// 형식에 맞게 입력하여 x좌표와 y좌표를 추출하여 숫자로 변환
				String zxc = "[" + x + ", " + y + "]"; // 변환 한 값을 eqlist와 비교하기 위해 String으로 저장
				if (eqlist.contains(zxc)) {
					// 입력한 좌표가 eqlist에 포함되어을 경우(이동 가능한 좌표일 경우)
					board[hx][hy] = 0; // 현재 위치는 0으로 만들고
					hx = x; // 이동할 x좌표
					hy = y; // 이동할 y좌표
					board[hx][hy] = temp; // 말이 이동하는 곳에 해당하는 값을 저장
					System.out.println("(" + changeB + ", " + hx + "," + hy + ") 으로 이동");
					eqlist.clear(); // 다음에 사용하기 위해 eqlist 값 삭제
					temp = 0; // 위와 동일
					break;
				} else { // eqlist에 포함되지 않앗을 경우 (이동못하는 좌표 입력시)
					System.out.println("해당 위치로는 이동할 수 없습니다.");
				}

			} else if (hancho.equals("cho")) { // 초나라 차례일 때 한나라와 동일 좌표 변수만 다름
				String xy;
				while (true) {
					System.out.print("\n이동할 위치(x,y): ");
					xy = BoardExample.scan.next();
					if (xy.substring(1, 2).equals(",")) {
						break;
					} else {
						System.out.println("형식에 맞춰서 입력해주세요.");
					}
				}
				int x = Integer.parseInt(xy.substring(0, 1));
				int y = Integer.parseInt(xy.substring(2));
				String zxc = "[" + x + ", " + y + "]";
				if (eqlist.contains(zxc)) {
					board[cx][cy] = 0;
					cx = x;
					cy = y;
					board[cx][cy] = temp;
					System.out.println("(" + changeB + ", " + cx + "," + cy + ") 으로 이동");
					eqlist.clear();
					temp = 0;
					break;
				} else {
					System.out.println("해당 위치로는 이동할 수 없습니다.");
				}
			}
		}
	}

	@Override
	public void hanAtt() { // 한나라 차례일 때
		System.out.println("한나라 공격");
		System.out.print("현재위치: ");
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] == 1) {
					System.out.print("(P1, " + i + ", " + j + ")  ");
				} else if (board[i][j] == 2) {
					System.out.print("(P2, " + i + ", " + j + ")  ");
				} else if (board[i][j] == 3) {
					System.out.print("(Q1, " + i + ", " + j + ")  ");
				} else if (board[i][j] == 4) {
					System.out.print("(Q2, " + i + ", " + j + ")  ");
				}
			} // 해당 말 별로 현재 위치 출력
		} // 꼭 P1이 처음은 아니게 됨
		System.out.println();
		System.out.print("이동할 말 선택: "); // 이동할 말 선택
		changeB = BoardExample.scan.next();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (changeB.equals("P1")) { // P1선택 시
					int[][] a = { { i + 1, j }, { i, j + 1 }, { i - 1, j }, { i, j - 1 } };
					// P 말이 이동할 수 있는 경우의 수를 담은 배열
					if (board[i][j] == 1) {
						hx = i; // 현재 한나라의 P1위치 저장
						hy = j;
						temp = board[i][j]; // 한나라 P1에 해당하는 값 저장
						System.out.print("이동 가능한 위치: ");
						for (int b = 0; b < a.length; b++) {
							if (a[b][0] >= 0 && a[b][1] >= 0) { // 판 크기 안에서 움직이는지 확인
								if (a[b][0] <= 3 && a[b][1] <= 3) {
									if (board[a[b][0]][a[b][1]] == 0 // 한나라의 말이 위치한 곳을 제외한 모든 곳 이동가능
											|| (board[a[b][0]][a[b][1]] >= 5 && board[a[b][0]][a[b][1]] < 9)) {
										eqlist.add(Arrays.toString(a[b])); // 이동가능한 좌표인지 확인하기 위해 리스트에 저장
										System.out.print(Arrays.toString(a[b])); // 이동가능한 위치 출력
									}
								}
							}
						}
						han--; // 한나라 턴을 넘기기 위해 -1
						hancho = "han"; // 한나라의 말을 이동하기 위해 han저장
						break; // 더 확인할 필요가 없기 때문에 break로 반복문 탈출
					}
				} else if (changeB.equals("P2")) {
					int[][] a = { { i + 1, j }, { i, j + 1 }, { i - 1, j }, { i, j - 1 } };
					if (board[i][j] == 2) {
						hx = i;
						hy = j;
						temp = board[i][j];
						System.out.print("이동 가능한 위치: ");
						for (int b = 0; b < a.length; b++) {
							if (a[b][0] >= 0 && a[b][1] >= 0) {
								if (a[b][0] <= 3 && a[b][1] <= 3) {
									if (board[a[b][0]][a[b][1]] == 0
											|| (board[a[b][0]][a[b][1]] >= 5 && board[a[b][0]][a[b][1]] < 9)) {
										eqlist.add(Arrays.toString(a[b]));
										System.out.print(Arrays.toString(a[b]));
									}
								}
							}
						}
						han--;
						hancho = "han";
						break;
					}
				} else if (changeB.equals("Q1")) {
					int[][] a = { { i + 1, j + 1 }, { i + 1, j - 1 }, { i - 1, j + 1 }, { i - 1, j - 1 } };
					if (board[i][j] == 3) {
						hx = i;
						hy = j;
						temp = board[i][j];
						System.out.print("이동 가능한 위치: ");
						for (int b = 0; b < a.length; b++) {
							if (a[b][0] >= 0 && a[b][1] >= 0) {
								if (a[b][0] <= 3 && a[b][1] <= 3) {
									if (board[a[b][0]][a[b][1]] == 0
											|| (board[a[b][0]][a[b][1]] >= 5 && board[a[b][0]][a[b][1]] < 9)) {
										eqlist.add(Arrays.toString(a[b]));
										System.out.print(Arrays.toString(a[b]));
									}
								}
							}
						}
						han--;
						hancho = "han";
						break;
					}
				} else if (changeB.equals("Q2")) {
					int[][] a = { { i + 1, j + 1 }, { i + 1, j - 1 }, { i - 1, j + 1 }, { i - 1, j - 1 } };
					if (board[i][j] == 4) {
						hx = i;
						hy = j;
						temp = board[i][j];
						System.out.print("이동 가능한 위치: ");
						for (int b = 0; b < a.length; b++) {
							if (a[b][0] >= 0 && a[b][1] >= 0) {
								if (a[b][0] <= 3 && a[b][1] <= 3) {
									if (board[a[b][0]][a[b][1]] == 0
											|| (board[a[b][0]][a[b][1]] >= 5 && board[a[b][0]][a[b][1]] < 9)) {
										eqlist.add(Arrays.toString(a[b]));
										System.out.print(Arrays.toString(a[b]));
									}
								}
							}
						}
						han--;
						hancho = "han";
						break;
					}
				}

			}
			if (han == 0) { // 한나라의 턴이 종료
				cho=4;		// 초나라 턴 설정 후 break로 반복문 탈출
				break;
			}
		}
	}

	@Override
	public void choAtt() {
		System.out.println("초나라 공격");
		System.out.print("현재위치: ");
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] == 5) {
					System.out.print("(P1, " + i + ", " + j + ")  ");
				} else if (board[i][j] == 6) {
					System.out.print("(P2, " + i + ", " + j + ")  ");
				} else if (board[i][j] == 7) {
					System.out.print("(Q1, " + i + ", " + j + ")  ");
				} else if (board[i][j] == 8) {
					System.out.print("(Q2, " + i + ", " + j + ")  ");
				}
			}
		}
		System.out.println();
		System.out.print("이동할 말 선택: ");
		changeB = BoardExample.scan.next();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (changeB.equals("P1")) { // P1선택 시
					int[][] a = { { i + 1, j }, { i, j + 1 }, { i - 1, j }, { i, j - 1 } };
					if (board[i][j] == 5) {
						cx = i;
						cy = j;
						temp = board[i][j];
						System.out.print("이동 가능한 위치: ");
						for (int b = 0; b < a.length; b++) {
							if (a[b][0] >= 0 && a[b][1] >= 0) {
								if (a[b][0] <= 3 && a[b][1] <= 3) {
									if (board[a[b][0]][a[b][1]] >= 0 && board[a[b][0]][a[b][1]] < 5) {
										eqlist.add(Arrays.toString(a[b]));
										System.out.print(Arrays.toString(a[b]));
									}
								}
							}
						}
						cho -= 2;
						hancho = "cho";
						break;
					}
				} else if (changeB.equals("P2")) {
					int[][] a = { { i + 1, j }, { i, j + 1 }, { i - 1, j }, { i, j - 1 } };
					if (board[i][j] == 6) {
						cx = i;
						cy = j;
						temp = board[i][j];
						System.out.print("이동 가능한 위치: ");
						for (int b = 0; b < a.length; b++) {
							if (a[b][0] >= 0 && a[b][1] >= 0) {
								if (a[b][0] <= 3 && a[b][1] <= 3) {
									if (board[a[b][0]][a[b][1]] >= 0 && board[a[b][0]][a[b][1]] < 5) {
										eqlist.add(Arrays.toString(a[b]));
										System.out.print(Arrays.toString(a[b]));
									}
								}
							}
						}
						cho -= 2;
						hancho = "cho";
						break;
					}
				} else if (changeB.equals("Q1")) {
					int[][] a = { { i + 1, j + 1 }, { i + 1, j - 1 }, { i - 1, j + 1 }, { i - 1, j - 1 } };
					if (board[i][j] == 7) {
						cx = i;
						cy = j;
						temp = board[i][j];
						System.out.print("이동 가능한 위치: ");
						for (int b = 0; b < a.length; b++) {
							if (a[b][0] >= 0 && a[b][1] >= 0) {
								if (a[b][0] <= 3 && a[b][1] <= 3) {
									if (board[a[b][0]][a[b][1]] >= 0 && board[a[b][0]][a[b][1]] < 5) {
										eqlist.add(Arrays.toString(a[b]));
										System.out.print(Arrays.toString(a[b]));
									}
								}
							}
						}
						cho -= 2;
						hancho = "cho";
						break;
					}
				} else if (changeB.equals("Q2")) {
					int[][] a = { { i + 1, j + 1 }, { i + 1, j - 1 }, { i - 1, j + 1 }, { i - 1, j - 1 } };
					if (board[i][j] == 8) {
						cx = i;
						cy = j;
						temp = board[i][j];
						System.out.print("이동 가능한 위치: ");
						for (int b = 0; b < a.length; b++) {
							if (a[b][0] >= 0 && a[b][1] >= 0) {
								if (a[b][0] <= 3 && a[b][1] <= 3) {
									if (board[a[b][0]][a[b][1]] >= 0 && board[a[b][0]][a[b][1]] < 5) {
										eqlist.add(Arrays.toString(a[b]));
										System.out.print(Arrays.toString(a[b]));
									}
								}
							}
						}
						cho -= 2;
						hancho = "cho";
						break;
					}
				}
			}
			if (cho == 2) {
				han = 1;
				break;
			}
		}
	}

	@Override
	public boolean GameEnd() {
		sum = 0; // 누적 되면 안되기 때문에 초기화
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				sum += board[i][j];
				list.add(String.valueOf(board[i][j]));
			} // 어느 나라의 어느 말이 잡혓는지 확인하기 위해 배열 값을 모두 더함
		} // 어느 나라의 승인지 알기 위해 list의 현재 말 값을 저장
		if (oldsum - sum == 1) {
			System.out.println("한나라의 P1이 잡혔습니다");
			oldsum = sum;
		} else if (oldsum - sum == 2) {
			System.out.println("한나라의 P2가 잡혔습니다");
			oldsum = sum;
		} else if (oldsum - sum == 3) {
			System.out.println("한나라의 Q1이 잡혔습니다");
			oldsum = sum;
		} else if (oldsum - sum == 4) {
			System.out.println("한나라의 Q2가 잡혔습니다");
			oldsum = sum;
		} else if (oldsum - sum == 5) {
			System.out.println("초나라의 P1이 잡혔습니다");
			oldsum = sum;
		} else if (oldsum - sum == 6) {
			System.out.println("초나라의 P2가 잡혔습니다");
			oldsum = sum;
		} else if (oldsum - sum == 7) {
			System.out.println("초나라의 Q1이 잡혔습니다");
			oldsum = sum;
		} else if (oldsum - sum == 8) {
			System.out.println("초나라의 Q2가 잡혔습니다");
			oldsum = sum;
		} else if (oldsum == sum) {
			System.out.println("잡지 못했음");
		} // 이전 값과 새로 더한 값의 차를 이용해 어느나라의 어느 말이 잡혓는지 확인
		while (true) { // 승부 체크를 위한 반복문 -> 사용하지 않을 경우 코드가 길어져서 사용
			if (!list.contains("1") && !list.contains("2") && !list.contains("3") && !list.contains("4")) {
				System.out.println("초나라 승리.");
				// list에 저장한 값중 한나라의 말에 해당하는 값이 없을 경우 초나라 승리
			} else if (!list.contains("5") && !list.contains("6") && !list.contains("7") && !list.contains("8")) {
				System.out.println("한나라 승리.");
				// list에 저장한 값중 초나라의 말에 해당하는 값이 없을 경우 한나라 승리
			} else {
				break;
			} // 말이 하나라도 남았을 경우 계속 진행
			System.out.print("다시 하시겠습니까? (y/n)");
			String yn = BoardExample.scan.next(); // 재시작을 위한 입력
			if (yn.equals("y")) { // 재시작을 원할 경우 false를 리턴해 처음부터 다시시작
				return false;
			} else { // n이나 다른 값을 입력했을 경우 종료하기 위함.
				System.out.println("프로그램 종료");
				BoardExample.run2 = false;
				return false;
			}
		}
		System.out.println();
		list.clear(); // 다시 사용하기 위한 초기화
		return true;
	}
}


