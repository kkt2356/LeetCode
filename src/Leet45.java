public class Leet45 {
    public static void main(String[] args) {
        Leet45 l = new Leet45();
        int[] a = {0};
        System.out.println("result : " + l.jump(a));

    }

    public int jump(int[] nums) {
        // 길이가 1인 경우에는 예외처리 함
        if(nums.length == 1) return 0;
        int jumps = 0, far = 0, end = 0;  //far : 현재 위치에서 점프했을때 가장 마지막 위치, end : 현재 뛰어야 할 시점.
        for(int i = 0; i < nums.length; i++) {
            far = Math.max(far, i + nums[i]);   // 현재 위치에서 점프했을때 가장 마지막 위치를 찾음
            // i + nums[i] 의 의미 : i만큼 이동한 상태에서 nums[i] 만큼 더 이동했을시의 길이
            // index 0 => 멀리 뛰었을때는 index 2에 위치
            // index 1 => 멀리 뛰었을때는 index 4에 위치
            // index 2 => 멀리 뛰었을때는 index 3에 위치
            if(i == end) {  //  i 가 뛰어야할 시점(end)에 도달했을 때 jumps 수 +1
                jumps++;
                end = far;    // 뛰어야 할 시점을 변경(인덱스 변경)

                if(end >= nums.length - 1) {
                    break;
                }
            }
        }
        return jumps;
    }
}
