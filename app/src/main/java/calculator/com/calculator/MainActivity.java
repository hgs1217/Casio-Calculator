package calculator.com.calculator;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R2.id.edit_formula)
    EditText editFormula;
    @BindView(R2.id.edit_show)
    EditText editShow;

    @BindView(R2.id.button_7)
    Button button7;
    @BindView(R2.id.button_8)
    Button button8;
    @BindView(R2.id.button_9)
    Button button9;
    @BindView(R2.id.button_del)
    Button buttonDel;
    @BindView(R2.id.button_ac)
    Button buttonAc;
    @BindView(R2.id.button_4)
    Button button4;
    @BindView(R2.id.button_5)
    Button button5;
    @BindView(R2.id.button_6)
    Button button6;
    @BindView(R2.id.button_multiply)
    Button buttonMultiply;
    @BindView(R2.id.button_divide)
    Button buttonDivide;
    @BindView(R2.id.button_1)
    Button button1;
    @BindView(R2.id.button_2)
    Button button2;
    @BindView(R2.id.button_3)
    Button button3;
    @BindView(R2.id.button_add)
    Button buttonAdd;
    @BindView(R2.id.button_minus)
    Button buttonMinus;
    @BindView(R2.id.button_0)
    Button button0;
    @BindView(R2.id.button_point)
    Button buttonPoint;
    @BindView(R2.id.button_10x)
    Button button10x;
    @BindView(R2.id.button_ans)
    Button buttonAns;
    @BindView(R2.id.button_equal)
    Button buttonEqual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initText();
        initOnClickListener();
    }

    private void initText() {
        editFormula.setText("1234");
        editShow.setText("0");
        button7.setText("7");
        button8.setText("8");
        button9.setText("9");
        buttonDel.setText("DEL");
        buttonAc.setText("AC");
        button4.setText("4");
        button5.setText("5");
        button6.setText("6");
        buttonMultiply.setText("×");
        buttonDivide.setText("÷");
        button1.setText("1");
        button2.setText("2");
        button3.setText("3");
        buttonAdd.setText("+");
        buttonMinus.setText("-");
        button0.setText("0");
        buttonPoint.setText(".");
        button10x.setText("×10^x");
        buttonAns.setText("Ans");
        buttonEqual.setText("=");
    }

    private void initOnClickListener() {
        editFormula.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editFormula.getWindowToken(), 0);
                }
            }
        });
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
