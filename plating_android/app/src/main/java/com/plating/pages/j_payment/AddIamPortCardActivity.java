package com.plating.pages.j_payment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.plating.R;
import com.plating.application.PlatingActivity;
import com.plating.helperAPI.DialogAPI;
import com.plating.helperAPI.ToastAPI;
import com.plating.network.j_payment.SetIamPortCardToServer;

import java.util.ArrayList;

/**
 * Created by Rooney on 16. 9. 2..
 */
public class AddIamPortCardActivity extends PlatingActivity implements View.OnClickListener {
    private LinearLayout privateCardLayout;
    private LinearLayout incCardLayout;
    private ArrayList<EditText> cardNumberList = new ArrayList<>(4);
    private EditText cardNumberFirst, cardNumberSecond, cardNumberThird, cardNumberFourth;
    private EditText cardPassword;
    private EditText cardExpiryMonth, cardExpiryYear;
    private LinearLayout cardPrivateButton, cardIncButton;
    private TextView cardPrivateText, cardIncText;
    private EditText cardPrivateBirth;
    private ArrayList<EditText> cardBirthList = new ArrayList<>(3);
    private EditText cardIncBirthFirst, cardIncBirthSecond, cardIncBirthThird;
    private int selectedIndex;
    private LinearLayout cardRegistButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.j_iamport_card_layout);
        initViews();
    }
    public void initViews() {
        // 카드 번호
        cardNumberFirst = (EditText) findViewById(R.id.card_number_first);
        // 첫 번째 카드 번호 포커스 및 키보드 오픈
        cardNumberFirst.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        cardNumberFirst.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setButtonEnabled();
                if (s.length() == 4) {
                    cardNumberSecond.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        cardNumberSecond = (EditText) findViewById(R.id.card_number_second);
        cardNumberSecond.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setButtonEnabled();
                if (s.length() == 4) {
                    cardNumberThird.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        cardNumberThird = (EditText) findViewById(R.id.card_number_third);
        cardNumberThird.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setButtonEnabled();
                if (s.length() == 4) {
                    cardNumberFourth.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        cardNumberFourth = (EditText) findViewById(R.id.card_number_fourth);
        cardNumberFourth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setButtonEnabled();
                if (s.length() == 4) {
                    cardPassword.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        cardNumberList.add(cardNumberFirst);
        cardNumberList.add(cardNumberSecond);
        cardNumberList.add(cardNumberThird);
        cardNumberList.add(cardNumberFourth);

        // 비밀번호
        cardPassword = (EditText) findViewById(R.id.card_password);
        cardPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setButtonEnabled();
                if (s.length() == 2) {
                    cardExpiryMonth.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // 유효기간
        cardExpiryMonth = (EditText) findViewById(R.id.card_expiry_month);
        cardExpiryMonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setButtonEnabled();
                if (s.length() == 2) {
                    cardExpiryYear.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        cardExpiryYear = (EditText) findViewById(R.id.card_expiry_year);
        cardExpiryYear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setButtonEnabled();
                if (s.length() == 2) {
                    if (selectedIndex == 0) {
                        cardPrivateBirth.requestFocus();
                    } else {
                        cardIncBirthFirst.requestFocus();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // 개인 / 법인 버튼
        cardPrivateButton = (LinearLayout) findViewById(R.id.card_private_button);
        cardIncButton = (LinearLayout) findViewById(R.id.card_inc_button);
        cardPrivateButton.setOnClickListener(this);
        cardIncButton.setOnClickListener(this);

        // 개인 / 법인 텍스트 뷰
        cardPrivateText = (TextView) findViewById(R.id.card_private_text);
        cardIncText = (TextView) findViewById(R.id.card_inc_text);

        // 개인, 법인 레이아웃
        privateCardLayout = (LinearLayout) findViewById(R.id.private_card_layout);
        incCardLayout = (LinearLayout) findViewById(R.id.inc_card_layout);
        privateCardLayout.setVisibility(View.VISIBLE);
        incCardLayout.setVisibility(View.GONE);

        // 개인 생년월일
        cardPrivateBirth = (EditText) findViewById(R.id.card_private_birth);
        cardPrivateBirth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setButtonEnabled();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // 법인 사업자 번호
        cardIncBirthFirst = (EditText) findViewById(R.id.card_inc_birth_first);
        cardIncBirthFirst.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setButtonEnabled();
                if (s.length() == 3) {
                    cardIncBirthSecond.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        cardIncBirthSecond = (EditText) findViewById(R.id.card_inc_birth_second);
        cardIncBirthSecond.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setButtonEnabled();
                if (s.length() == 2) {
                    cardIncBirthThird.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        cardIncBirthThird = (EditText) findViewById(R.id.card_inc_birth_third);
        cardIncBirthThird.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setButtonEnabled();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        cardBirthList.add(cardIncBirthFirst);
        cardBirthList.add(cardIncBirthSecond);
        cardBirthList.add(cardIncBirthThird);

        cardRegistButton = (LinearLayout) findViewById(R.id.card_regist_button);
        cardRegistButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.card_private_button:
                cardPrivateButton.setBackgroundResource(R.drawable.corner_primary_primary);
                cardPrivateText.setTextColor(getResources().getColor(R.color.white));
                cardIncButton.setBackgroundResource(R.drawable.corner_white_primary);
                cardIncText.setTextColor(getResources().getColor(R.color.colorPrimary));
                privateCardLayout.setVisibility(View.VISIBLE);
                incCardLayout.setVisibility(View.GONE);
                selectedIndex = 0;
                setButtonEnabled();
                break;
            case R.id.card_inc_button:
                cardPrivateButton.setBackgroundResource(R.drawable.corner_white_primary);
                cardPrivateText.setTextColor(getResources().getColor(R.color.colorPrimary));
                cardIncButton.setBackgroundResource(R.drawable.corner_primary_primary);
                cardIncText.setTextColor(getResources().getColor(R.color.white));
                privateCardLayout.setVisibility(View.GONE);
                incCardLayout.setVisibility(View.VISIBLE);
                selectedIndex = 1;
                setButtonEnabled();
                break;
            case R.id.card_regist_button:
                registCard();
                break;
        }
    }

    public boolean isRegistAvailable() {
        boolean isAvailable = true;
        // 카드번호 체크
        for(int i = 0; i < cardNumberList.size(); i++) {
            EditText editText = cardNumberList.get(i);
            if(editText.getText().length() != 4) {
                return false;
            }
        }
        // 비밀번호 체크
        if(cardPassword.getText().length() != 2) {
            return false;
        }
        // 유효기간 체크
        if(cardExpiryMonth.getText().length() != 2) {
            return false;
        }
        if(cardExpiryYear.getText().length() != 2) {
            return false;
        }
        // 생년월일, 사업자 번호 체크
        if(selectedIndex == 0) {
            if(cardPrivateBirth.getText().length() != 6) {
                return false;
            }
        } else {
            for(int i = 0; i < cardBirthList.size(); i++) {
                EditText editText = cardBirthList.get(i);
                int length = editText.getText().length();
                if(i == 0 && length != 3) {
                    return false;
                }
                if(i == 1 && length != 2) {
                    return false;
                }
                if(i == 2 && length != 5) {
                    return false;
                }
            }
        }
        return isAvailable;
    }

    public void setButtonEnabled() {
        if(!isRegistAvailable()) {
            cardRegistButton.setBackgroundResource(R.drawable.corner_grey300_grey300);
        } else {
            cardRegistButton.setBackgroundResource(R.drawable.corner_primary_primary);
        }
    }

    public void registCard() {
        if(!isRegistAvailable()) {
            DialogAPI.showDialog(this, "알림", "카드 정보를 모두 입력해주세요.", "확인", null);
        } else {
            StringBuffer cardNumber = new StringBuffer();
            for(int i = 0; i < cardNumberList.size(); i++) {
                EditText editText = cardNumberList.get(i);
                cardNumber.append(editText.getText().toString());
                cardNumber.append("-");
            }

            String expiry = "20" + cardExpiryYear.getText().toString() + "-" + cardExpiryMonth.getText().toString();
            String passwordPre2Digit = cardPassword.getText().toString();
            StringBuffer birth = new StringBuffer();
            if(selectedIndex == 0) {
                birth.append(cardPrivateBirth.getText().toString());
            } else {
                for(int i = 0; i < cardBirthList.size(); i++) {
                    EditText editText = cardBirthList.get(i);
                    birth.append(editText.getText().toString());
                }
            }
            Log.d(LOG_TAG, cardNumber.substring(0, cardNumber.length() - 1));
            Log.d(LOG_TAG, expiry);
            Log.d(LOG_TAG, passwordPre2Digit);
            Log.d(LOG_TAG, birth.toString());

            SetIamPortCardToServer.sendDataToServer(mContext, mRequestQueue, cardNumber.substring(0, cardNumber.length() - 1), expiry, birth.toString(), passwordPre2Digit);
        }
    }

    public void registCard_Callback(int code, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(code == 200) {

            builder.setTitle("카드 등록 성공!");
            builder.setMessage("결제를 진행해 주세요 :)");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

        } else if(code == 400) {
            builder.setTitle("카드 등록 실패!");
            builder.setMessage(message);
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
        }
        builder.create();
        builder.show();
    }
}
