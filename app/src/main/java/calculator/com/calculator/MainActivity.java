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

    @BindView(R.id.edit_formula)
    EditText editFormula;
    @BindView(R.id.edit_show)
    EditText editShow;

    @BindView(R.id.button_7)
    Button button7;
    @BindView(R.id.button_8)
    Button button8;
    @BindView(R.id.button_9)
    Button button9;
    @BindView(R.id.button_del)
    Button buttonDel;
    @BindView(R.id.button_ac)
    Button buttonAc;
    @BindView(R.id.button_4)
    Button button4;
    @BindView(R.id.button_5)
    Button button5;
    @BindView(R.id.button_6)
    Button button6;
    @BindView(R.id.button_multiply)
    Button buttonMultiply;
    @BindView(R.id.button_divide)
    Button buttonDivide;
    @BindView(R.id.button_1)
    Button button1;
    @BindView(R.id.button_2)
    Button button2;
    @BindView(R.id.button_3)
    Button button3;
    @BindView(R.id.button_plus)
    Button buttonPlus;
    @BindView(R.id.button_minus)
    Button buttonMinus;
    @BindView(R.id.button_0)
    Button button0;
    @BindView(R.id.button_point)
    Button buttonPoint;
    @BindView(R.id.button_10x)
    Button button10x;
    @BindView(R.id.button_ans)
    Button buttonAns;
    @BindView(R.id.button_equal)
    Button buttonEqual;
    @BindView(R.id.button_mod)
    Button buttonMod;
    @BindView(R.id.button_get_int)
    Button buttonGetInt;
    @BindView(R.id.button_left_bracket)
    Button buttonLeftBracket;
    @BindView(R.id.button_right_bracket)
    Button buttonRightBracket;
    @BindView(R.id.button_power)
    Button buttonPower;

    private static final char SIGN_NUM_SEPARATOR = ',';
    private static final char SIGN_POINT = '.';
    private static final char SIGN_PLUS = '+';
    private static final char SIGN_MINUS = '-';
    private static final char SIGN_MULTIPLY = '*';
    private static final char SIGN_DIVIDE = '/';
    private static final char SIGN_MOD = '%';
    private static final char SIGN_POWER = '^';
    private static final char SIGN_GET_INT = '|';

    private double answer = 0;

    private Button[] viewsSameInExp;
    private Button[] viewsDiffInExp;
    private String[] stringsSameInExp;
    private String[] stringsDiffInExp;
    private String[] signOrigin = new String[] {"×", "÷", "\\[", "]", "Mod"};
    private String[] signReplace = new String[] {String.valueOf(SIGN_MULTIPLY), String.valueOf(SIGN_DIVIDE),
            "("+SIGN_GET_INT, ")", String.valueOf(SIGN_MOD)};

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
                                    buttonMultiply, buttonDivide, buttonPoint, buttonAns, buttonMod,
                                    buttonLeftBracket, buttonRightBracket, buttonPower};
        viewsDiffInExp = new Button[] {buttonDel, buttonAc, button10x, buttonEqual, buttonGetInt};
        stringsSameInExp = new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "+",
                                    "-", "×", "÷", ".", "Ans", "Mod", "(", ")", "^"};
        stringsDiffInExp = new String[] {"DEL", "AC", "×10^x", "=", "[ ]"};
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
            setSoftInputShownOnFocus.invoke(editShow, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initOnClickListener() {
        for (int i=0; i<viewsSameInExp.length; ++i) {
            final String exp = stringsSameInExp[i];
            viewsSameInExp[i].setOnClickListener((View view) -> {
                if (editFormula.hasFocus()) {
                    int index = editFormula.getSelectionStart();
                    Editable editable = editFormula.getText();
                    editable.insert(index, exp);
                }
            });
        }
        buttonDel.setOnClickListener((View view) -> {
            /* 多字符符号需进行额外设定 */
            if (editFormula.hasFocus()) {
                int index = editFormula.getSelectionStart();
                Editable editable = editFormula.getText();
                if (index > 0) {
                    String editStr = editable.toString();
                    if (index > 2) {
                        if (editStr.substring(index - 3, index).equals("Ans")) {
                            editable.delete(index - 3, index);
                        } else if (editStr.substring(index - 3, index).equals("Mod")) {
                            editable.delete(index - 3, index);
                        } else {
                            editable.delete(index - 1, index);
                        }
                    } else {
                        editable.delete(index - 1, index);
                    }
                }
            }
        });
        buttonAc.setOnClickListener((View view) -> {
            editFormula.setText("");
        });
        button10x.setOnClickListener((View view) -> {
            if (editFormula.hasFocus()) {
                int index = editFormula.getSelectionStart();
                Editable editable = editFormula.getText();
                editable.insert(index, "×(10^)");
                index = editFormula.getSelectionStart();
                editFormula.setSelection(index - 1);
            }
        });
        buttonEqual.setOnClickListener((View view) -> {
            if (editFormula.hasFocus()) {
                String postfixExp = initExpression(editFormula.getText().toString());
                editShow.setText(calculate(postfixExp));
            }
        });
        buttonGetInt.setOnClickListener((View view) -> {
            if (editFormula.hasFocus()) {
                int index = editFormula.getSelectionStart();
                Editable editable = editFormula.getText();
                editable.insert(index, "[]");
                index = editFormula.getSelectionStart();
                editFormula.setSelection(index - 1);
            }
        });
    }

    private String initExpression(String exp) {
        /* 将中缀表达式改成后缀表达式 */

        String postfixExp = "", excExp = "";
        boolean canNumEnd = false; // 数字结束的判断
        boolean plusNeedZero = true; // 加号前补0的判断
        boolean canRightBracket = false; // 右括号是否合法的判断
        int immediateBracket = 0; // 减号后紧跟括号的数量
        Stack<String> stack = new Stack<>();
        // 转化Ans为数字
        exp = exp.replaceAll("Ans", String.valueOf(answer));
        // 转化特殊符号为内部符号形式保存
        for (int i=0; i<signOrigin.length; ++i) {
            exp = exp.replaceAll(signOrigin[i], signReplace[i]);
        }
        // 双减号替换为加号
        exp = exp.replaceAll("--", "+");
        // 减号替换为加号和负号
        exp = exp.replaceAll("-", "+-");
        // 当+前为数字、小数点或右括号时，不用补（0 ）；其他情况，需补（0 ），其中右括号加在+后的数字后
        for (int i=0; i<exp.length(); ++i) {
            char s = exp.charAt(i);
            if (s >= '0' && s <= '9' || s == SIGN_POINT) {
                plusNeedZero = false;
                canNumEnd = true;
            } else {
                if (canNumEnd && canRightBracket && immediateBracket == 0) {
                    excExp = excExp + ")";
                    canRightBracket = false;
                }
                if (s == ')') {
                    plusNeedZero = false;
                    immediateBracket--;
                    if (canRightBracket && immediateBracket == 0) {
                        excExp = excExp + ")";
                        canRightBracket = false;
                    }
                } else if (s == '(') {
                    immediateBracket++;
                } else if (s == SIGN_PLUS) {
                    if (plusNeedZero) {
                        excExp = excExp + "(0";
                        canRightBracket = true;
                    }
                    plusNeedZero = true;
                } else {
                    plusNeedZero = true;
                }
                canNumEnd = false;
            }
            excExp = excExp + s;
        }
        if (canNumEnd && canRightBracket) {
            excExp = excExp + ")";
        }

        Log.d("TEST Exp", excExp);
        try {
            for (int i=0; i<excExp.length(); ++i) {
                char s = excExp.charAt(i);
                if (s >= '0' && s <= '9' || s == SIGN_POINT) {
                    postfixExp = postfixExp + s;
                    canNumEnd = true;
                } else {
                    if (canNumEnd) {
                        postfixExp = postfixExp + SIGN_NUM_SEPARATOR;
                    }
                    canNumEnd = false;
                    if (s == '(') {
                        stack.push(String.valueOf(s));
                    } else if (s == SIGN_PLUS || s == SIGN_MINUS || s == SIGN_MULTIPLY || s == SIGN_DIVIDE
                            || s == SIGN_POWER || s == SIGN_MOD || s == SIGN_GET_INT) {
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
        } catch (Exception e) {
            e.printStackTrace();
            return "Syntax Error";
        }

        if (canNumEnd) {
            postfixExp = postfixExp + SIGN_NUM_SEPARATOR;
        }
        while (!stack.isEmpty()) {
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
        try {
            for (int i=0; i<postfixExp.length(); ++i) {
                char s = postfixExp.charAt(i);
                if (s >= '0' && s <= '9' || s == SIGN_POINT) {
                    tmpNum = tmpNum + s;
                } else {
                    switch (s) {
                        case SIGN_NUM_SEPARATOR:
                            if (tmpNum.length() > 0) {
                                stack.push(Double.parseDouble(tmpNum));
                            }
                            tmpNum = "";
                            break;
                        case SIGN_PLUS:
                            b = stack.pop();
                            a = stack.pop();
                            stack.push(a+b);
                            break;
                        case SIGN_MINUS:
                            a = stack.pop();
                            stack.push(-a);
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
                        case SIGN_MOD:
                            b = stack.pop();
                            a = stack.pop();
                            int intA = a.intValue(), intB = b.intValue();
                            if (intA != a || intB != b) {
                                return "Mod Error";
                            }
                            double m = Double.valueOf(intA % intB);
                            stack.push(m);
                            break;
                        case SIGN_POWER:
                            b = stack.pop();
                            a = stack.pop();
                            stack.push(Math.pow(a, b));
                            break;
                        case SIGN_GET_INT:
                            a = stack.pop();
                            b = Double.valueOf(a.intValue());
                            stack.push(b);
                            break;
                        default:
                            Log.e("SIGN ERROR", "");
                            return "Syntax Error";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Syntax Error";
        }

        if (stack.size() == 1) {
            answer = stack.pop();
            if ((int)answer == answer) {
                return String.valueOf((int)answer);
            }
            return String.valueOf(answer);
        }
        return "Syntax Error";
    }

    private int operatorComparator(String a, String b) {
        int x=0, y=0;
        if (a.equals(String.valueOf(SIGN_GET_INT))) {
            x = 1;
        } else if (a.equals("+")) {
            x = 2;
        } else if (a.equals("*") || a.equals("/")) {
            x = 3;
        } else if (a.equals("^")) {
            x = 5;
        } else if (a.equals("-")) {
            x = 6;
        }
        if (b.equals(String.valueOf(SIGN_GET_INT))) {
            y = 1;
        } else if (b.equals("+")) {
            y = 2;
        } else if (b.equals("*") || b.equals("/")) {
            y = 3;
        } else if (b.equals("^")) {
            y = 5;
        } else if (b.equals("-")) {
            y = 6;
        }
        return (int)Math.signum(x-y);
    }
}
