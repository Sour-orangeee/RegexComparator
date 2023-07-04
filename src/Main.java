public class Main {

    public static String addChar(String str, char ch, int position) {
        int len = str.length();
        char[] updatedArr = new char[len + 1];
        str.getChars(0, position, updatedArr, 0);
        updatedArr[position] = ch;
        str.getChars(position, len, updatedArr, position + 1);
        return new String(updatedArr);
    }

    public static String dotAdder(String input) {
        String output = null;
        for (int i = 0; i < input.length() - 1; i++) {
            if (Character.isLowerCase(input.charAt(i)) && Character.isLowerCase(input.charAt(i + 1))) {
                output = addChar(input, '.', i+1);
                System.out.println("2");
                return output;
            } else {
                System.out.println("1");
                output = input;
            }
        }
        return output;
    }


    public static void main(String[] args) {
        String s= "a*b";
        System.out.println(dotAdder(s));
    }
}
