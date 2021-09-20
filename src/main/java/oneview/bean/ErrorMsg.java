package oneview.bean;

import java.util.ArrayList;
import java.util.List;

public class ErrorMsg {
    private List<String> errorList;
    public ErrorMsg(){
        errorList = new ArrayList<>();
    }

    public void addErrorMsg(String msg){
        errorList.add(msg);
    }

    public List<String> getErrorList(){
        return errorList;
    }

    public boolean isEmpty(){
        return errorList.size() == 0? true: false;
    }

    @Override
    public String toString() {
        String errorMsgStr = "";
        for (int i=0; i< errorList.size(); i++){
            errorMsgStr+=errorList.get(i)+"\n";
        }
        return  errorMsgStr;
    }

    public void reset() {
        errorList = new ArrayList<>();
    }
}
