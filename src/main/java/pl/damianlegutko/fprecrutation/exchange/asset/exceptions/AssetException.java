package pl.damianlegutko.fprecrutation.exchange.asset.exceptions;

import pl.damianlegutko.fprecrutation.commonExceptions.BaseException;

public class AssetException extends BaseException {

    AssetException(String errorMessage) {
        super(errorMessage);
    }
}