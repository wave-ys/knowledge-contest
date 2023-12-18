package cn.edu.seu.manualcontest.exception;

public class UserAlreadyExistException extends ForbiddenException {

    public static UserAlreadyExistException bySid(String sid) {
        return new UserAlreadyExistException(String.format("用户已存在，学号：%s", sid));
    }

    public static UserAlreadyExistException byCardId(String cardId) {
        return new UserAlreadyExistException(String.format("用户已存在，一卡通号：%s", cardId));
    }

    private UserAlreadyExistException(String message) {
        super(message);
    }
}
