//
//  ListViewActivity.kt
//  Twister
//
//  Created by Amr Aboelela on 10/26/18.
//  Copyright © 2018 Twister Org. All rights reserved.
//

package net.twister.twister

class LoginActivity : BaseActivity()/*
    RelativeLayout fragmentLayout;

    TextView status;
    static EditText phoneNumber;
    static Button skipVerify;
    static Button login;

    ProgressDialog loading;

    public static String cookie = "";
    public static String uId = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseActivity = (BaseActivity)getActivity();
        mApiService = UtilsApi.getAPIService(); // meng-init yang ada di package apihelper
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_login_fragment, container, false);

        // define the view components
        status = (TextView) view.findViewById(R.id.status);
        phoneNumber = (EditText) view.findViewById(R.id.phone_number);
        skipVerify = (Button) view.findViewById(R.id.skip_verify);
        login = (Button) view.findViewById(R.id.login);
        final LoginFragment thisFragment = this;
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HttpUtil(baseActivity, thisFragment).login(phoneNumber.getText().toString().trim(), false);
            }
        });
        skipVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HttpUtil(baseActivity, thisFragment).login(phoneNumber.getText().toString().trim(), true);
            }
        });

        return view;
    }

    private void resetAllSharedPreferences() {
        //SharedPreferencesType.getInstance(LoginActivity.this).reset();
        SharedPreferenceUser.getInstance(LoginActivity.this).reset();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);

        // define the view components
        fragmentLayout = (RelativeLayout) findViewById(R.id.fragmentLayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do something you want
                onBackPressed();
            }
        });

        //View view = inflater.inflate(R.layout.main_login_fragment, container, false);

        // define the view components
        status = findViewById(R.id.status);
        phoneNumber = findViewById(R.id.phone_number);
        skipVerify = findViewById(R.id.skip_verify);
        login = findViewById(R.id.login);
        if (TwisterEnvironment.production) {
            skipVerify.setVisibility(View.GONE);
        }
        final LoginActivity thisActivity = this;

        //return view;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("onActivityResult", "Activity Successful");

        android.app.Fragment fragment = getFragmentManager().findFragmentByTag("loginFragment");
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    // MARK: - Delegates
*/