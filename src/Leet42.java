public class Leet42 {
    public static void main(String[] args) {
        Leet42 a = new Leet42();
        int[] b = {4,2,0,3,2,5};
        System.out.println(a.trap(b));
    }

    public int trap(int[] height) {
        int total = 0, high1 = 0, high2 = 0;
        for(int l = 0, r = height.length - 1; l < r;) {
            if(height[l] < height[r]) { //  0 5
                high1 = Math.max(high1, height[l]); // 4
                total += high1 - height[l++]; // 2
            } else {
                high2 = Math.max(high2, height[r]);
                total += high2 - height[r--];
            }
        }
        return total;
    }
}
