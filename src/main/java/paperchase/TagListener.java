package paperchase;

import com.google.zxing.Result;

public interface TagListener {
    void onTag(Result result, float orientation);
}