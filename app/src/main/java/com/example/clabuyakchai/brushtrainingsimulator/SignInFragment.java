package com.example.clabuyakchai.brushtrainingsimulator;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clabuyakchai.brushtrainingsimulator.api.ApiService;
import com.example.clabuyakchai.brushtrainingsimulator.api.Client;
import com.example.clabuyakchai.brushtrainingsimulator.model.UserLogin;
import com.example.clabuyakchai.brushtrainingsimulator.sharedpreferences.Preferences;
import com.example.clabuyakchai.brushtrainingsimulator.stateinternet.StateInternet;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Clabuyakchai on 04.05.2018.
 */

public class SignInFragment extends Fragment {

    private TextView mUsername;
    private TextView mPassword;
    private TextView mSignup;

    private Button mSignInButton;

    private FragmentTransaction mFragmentTransaction;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin, container, false);

        mUsername = view.findViewById(R.id.signinUserName);
        mPassword = view.findViewById(R.id.signinPassword);
        mSignup = view.findViewById(R.id.signupTextView);

        mSignInButton = view.findViewById(R.id.signinButton);

        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new SignUpFragment();
                mFragmentTransaction = getFragmentManager().beginTransaction();
                mFragmentTransaction.replace(R.id.fragment_container, fragment)
                        .addToBackStack("SignIn")
                        .commit();
            }
        });

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(StateInternet.hasConnection(getActivity())){
                    UserLogin user = new UserLogin();
                    user.setUsername(mUsername.getText().toString());
                    user.setPassword(mPassword.getText().toString());

                    ApiService service = Client.getApiService();
                    Call<ResponseBody> call = service.signin(user);

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response.isSuccessful()){
                                String token = null;
                                try {
                                    token = response.body().string();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                Preferences.setTokenSharedPreferences(getActivity(), "Bearer " + token);
                                Preferences.setUsernameSharedPreferences(getActivity(), user.getUsername().toString());

                                startActivity(MainActivity.newIntent(getActivity()));

                            } else {
                                Toast.makeText(getActivity(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(getActivity(), "onFailure", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), R.string.error_internet_connection, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
