public class Leet44 {
    public static void main(String[] args) {
        // s문자열과 p패턴이 주어질때 . 와 *가 지원되는 정규식의 표현을 구하라
        // . 어느 하나의 문자와도 매칭됨
        // * *의 바로 앞에 있는 문자와 0번이상 매칭됨

    }

    public boolean isMatch(String s, String p) {
        boolean[][] match = new boolean[s.length() + 1][p.length() + 1];
        match[s.length()][p.length()] = true;
        for (int i = p.length() - 1; i >= 0; i--) {
            if(p.charAt(i) != "*")
                break;
            else
                match[s.length()][i] = true;
        }
        for(int i = s.length() - 1; i >=0; i--){
            for(int j = p.length() - 1; j >= 0; j--) {
                if(s.charAt(i) == p.charAt(j) || p.charAt(j) == '?')
                    match[i][j] = match[i+1][j+1];
                else if(p.charAt(j) == '*')
                    match[i][j] = match[i+1][j] || match[i][j+1];
                else
                    match[i][j] = false;
            }
        }
        return match[0][0];

    }
}
