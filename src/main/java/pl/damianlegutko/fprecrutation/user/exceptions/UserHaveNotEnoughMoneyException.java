package pl.damianlegutko.fprecrutation.user.exceptions;

import pl.damianlegutko.fprecrutation.Messages;

import java.math.BigDecimal;

public class UserHaveNotEnoughMoneyException extends UserException {

    public UserHaveNotEnoughMoneyException(String userName, BigDecimal moneyAmount) {
        super(Messages.get("user.exp.haveNotEnoughMoney", userName, moneyAmount));
        this.errorCode = ErrorCode.USER_HAVE_NOT_ENOUGH_MONEY;
    }
}