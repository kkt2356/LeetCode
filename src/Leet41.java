public class Leet41 {
    public static void main(String[] args) {
        /*
            O(n)
            [1,2,0] => 0,1,2 => 3
            [3,4,-1,1] => -1, 1, 3, 4 => 2
            [7,8,9,11,12] => 1
         */
        int[] t = {3,4,-1,1};
        Leet41 a = new Leet41();
        a.firstMissingPositive(t);

    }

    public int firstMissingPositive(int[] nums) {
        int i = 0;
        while(i < nums.length){         // [3,4,-1,1] length:4
            if(nums[i] >= 1 && nums[i] <= nums.length && nums[nums[i]-1] != nums[i]) {
                // i : 1 => nums[1] : 4
                swap(nums, i, nums[i]-1);   // [-1,1,3,4]
            }else{
                i++;    // i = 4
            }
        }
        i = 0;
        for(int j=0; j < nums.length; j++) {
            System.out.println(nums[j]);
        }
        while(i < nums.length && nums[i] == i+1) i++;   //  7 == 1
        return i+1; //
    }

    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
}
