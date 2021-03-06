import java.util.*;
public class Leet46 {
    public static void main(String[] args) {
        /*
        Input: nums = [1,2,3]
        Output: [[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
        ==> 모든 경우를 다 봐야하므로 backtracking이 좋아보임
         */


    }
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> list = new ArrayList<>();   // 리턴할 list를 선언.
        backtracking(list, new ArrayList<>(), nums);
        return list;
    }

    private void backtracking(List<List<Integer>> list, List<Integer> tempList, int[] nums) {
        if(tempList.size() == nums.length) {
            list.add(new ArrayList<>(tempList));
        } else {
            for(int i = 0; i < nums.length; i++) {
                if(tempList.contains(nums[i])) continue;
                tempList.add(nums[i]);
                backtracking(list, tempList, nums);
                tempList.remove(tempList.size() - 1);
            }
        }
    }
}
