package calculator.com.calculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import java.lang.reflect.Method;
import java.util.Stack;

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
    @BindView(R2.id.button_plus)
    Button buttonPlus;
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

    private static final char SIGN_NUM_SEPARATOR = ',';
    private static final char SIGN_POINT = '.';
    private static final char SIGN_ANS = '[';
    private static final char SIGN_PLUS = '+';
    private static final char SIGN_MINUS = '-';
    private static final char SIGN_MULTIPLY = '*';
    private static final char SIGN_DIVIDE = '/';

    private Button[] viewsSameInExp;
    private Button[] viewsDiffInExp;
    private String[] stringsSameInExp;
    private String[] stringsDiffInExp;
    private String[] signOrigin = new String[] {"Ans", "×", "÷"};
    private char[] signReplace = new char[] {SIGN_ANS, SIGN_MULTIPLY, SIGN_DIVIDE};

    private double answer = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initData();
        initText();
        initHideSoftInput();
        initOnClickListener();
    }

    private void initData() {
        viewsSameInExp = new Button[] {button0, button1, button2, button3, button4, button5,
                                    button6, button7, button8, button9, buttonPlus, buttonMinus,
                                    buttonMultiply, buttonDivide, buttonPoint, buttonAns};
        viewsDiffInExp = new Button[] {buttonDel, buttonAc, button10x, buttonEqual};
        stringsSameInExp = new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "+",
                                    "-", "×", "÷", ".", "Ans"};
        stringsDiffInExp = new String[] {"DEL", "AC", "×10^x", "="};
    }

    private void initText() {
        for (int i=0; i<viewsSameInExp.length; ++i) {
            viewsSameInExp[i].setText(stringsSameInExp[i]);
        }
        for (int i=0; i<viewsDiffInExp.length; ++i) {
            viewsDiffInExp[i].setText(stringsDiffInExp[i]);
        }
    }

    private void initHideSoftInput() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        try {
            Class<EditText> cls = EditText.class;
            Method setSoftInputShownOnFocus;
            setSoftInputShownOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
            setSoftInputShownOnFocus.setAccessible(true);
            setSoftInputShownOnFocus.invoke(editFormula, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initOnClickListener() {
        for (int i=0; i<viewsSameInExp.length; ++i) {
            final String exp = stringsSameInExp[i];
            viewsSameInExp[i].setOnClickListener((View view) -> {
                int index = editFormula.getSelectionStart();
                Editable editable = editFormula.getText();
                editable.insert(index, exp);
            });
        }
        buttonDel.setOnClickListener((View view) -> {
            int index = editFormula.getSelectionStart();
            Editable editable = editFormula.getText();
            if (index > 0) {
                String editStr = editable.toString();
                if (index > 2) {
                    if (editStr.substring(index - 3, index).equals("Ans")) {
                        editable.delete(index - 3, index);
                    } else {
                        editable.delete(index - 1, index);
                    }
                } else {
                    editable.delete(index - 1, index);
                }
            }
        });
        buttonAc.setOnClickListener((View view) -> {
            editFormula.setText("");
        });
        button10x.setOnClickListener((View view) -> {
            int index = editFormula.getSelectionStart();
            Editable editable = editFormula.getText();
            editable.insert(index, "×(10^)");
            index = editFormula.getSelectionStart();
            editFormula.setSelection(index - 1);
        });
        buttonEqual.setOnClickListener((View view) -> {
            String postfixExp = initExpression(editFormula.getText().toString());
            editShow.setText(calculate(postfixExp));
        });
    }

    private String initExpression(String exp) {
        /* 将中缀表达式改成后缀表达式 */

        String postfixExp = "";
        boolean canNumEnd = false;
        Stack<String> stack = new Stack<>();
        // 转化特殊符号为内部符号形式保存
        for (int i=0; i<signOrigin.length; ++i) {
            exp = exp.replaceAll(signOrigin[i], String.valueOf(signReplace[i]));
        }
        Log.d("TEST Exp", exp);
        for (int i=0; i<exp.length(); ++i) {
            char s = exp.charAt(i);
            if (s >= '0' && s <= '9' || s == SIGN_POINT) {
                postfixExp = postfixExp + s;
                canNumEnd = true;
            } else {
                if (canNumEnd) {
                    postfixExp = postfixExp + SIGN_NUM_SEPARATOR;
                }
                canNumEnd = false;
                if (s == SIGN_ANS) {
                    postfixExp = postfixExp + s;
                } else if (s == '(') {
                    stack.push(String.valueOf(s));
                } else if (s == SIGN_PLUS || s == SIGN_MINUS || s == SIGN_MULTIPLY || s == SIGN_DIVIDE) {
                    while (true) {
                        if (stack.isEmpty() || stack.peek().equals("(") ||
                                operatorComparator(String.valueOf(s), stack.peek()) > 0) {
                            stack.push(String.valueOf(s));
                            break;
                        } else {
                            postfixExp = postfixExp + stack.pop();
                        }
                    }
                } else if (s == ')') {
                    while (!stack.peek().equals("(")) {
                        postfixExp = postfixExp + stack.pop();
                    }
                    stack.pop();
                }
            }
        }
        while (!stack.isEmpty()) {
            if (canNumEnd) {
                postfixExp = postfixExp + SIGN_NUM_SEPARATOR;
            }
            canNumEnd = false;
            postfixExp = postfixExp + stack.pop();
        }
        Log.d("TEST PostfixExp", postfixExp);
        return postfixExp;
    }

    private String calculate(String postfixExp) {
        /* 根据后缀表达式进行计算 */
        Stack<Double> stack = new Stack<>();
        String tmpNum = "";
        Double a, b;
        for (int i=0; i<postfixExp.length(); ++i) {
            char s = postfixExp.charAt(i);
            if (s >= '0' && s <= '9' || s == SIGN_POINT) {
                tmpNum = tmpNum + s;
            } else {
                switch (s) {
                    case SIGN_NUM_SEPARATOR:
                        Log.d("TEST", String.valueOf(Double.parseDouble(tmpNum)));
                        stack.push(Double.parseDouble(tmpNum));
                        tmpNum = "";
                        break;
                    case SIGN_ANS:
                        stack.push(answer);
                        break;
                    case SIGN_PLUS:
                        Log.d("TEST", String.valueOf(stack.size()));
                        b = stack.pop();
                        a = stack.pop();
                        stack.push(a+b);
                        break;
                    case SIGN_MINUS:
                        b = stack.pop();
                        a = stack.pop();
                        stack.push(a-b);
                        break;
                    case SIGN_MULTIPLY:
                        b = stack.pop();
                        a = stack.pop();
                        stack.push(a*b);
                        break;
                    case SIGN_DIVIDE:
                        b = stack.pop();
                        a = stack.pop();
                        stack.push(a/b);
                        break;
                    default:
                        Log.e("SIGN ERROR", "");
                        break;
                }
            }
        }
        if (stack.size() == 1) {
            answer = stack.pop();
            return String.valueOf(answer);
        }
        return "Syntax Error";
    }

    private int operatorComparator(String a, String b) {
        int x=0, y=0;
        if (a.equals("+") || a.equals("-")) {
            x = 1;
        } else if (a.equals("*") || a.equals("/")) {
            x = 2;
        }
        if (b.equals("+") || b.equals("-")) {
            y = 1;
        } else if (b.equals("*") || b.equals("/")) {
            y = 2;
        }
        return (int)Math.signum(x-y);
    }
}
