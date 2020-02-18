public class Main {
    public static void main(String[] args) throws NotAllowedActionException, NotSharedCategoryException,
            DataNotFoundException, DataAlreadyExistsException, FriendNotFoundException,
            CategoryNotFoundException, CategoryAlreadyExistsException {
        Test t = new Test();
        if(args[0].equals("1")) {
            t.test1();
        } else {
            t.test2();
        }
    }
}
