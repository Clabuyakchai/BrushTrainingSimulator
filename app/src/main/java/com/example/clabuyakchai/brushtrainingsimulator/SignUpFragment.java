package com.example.clabuyakchai.brushtrainingsimulator;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clabuyakchai.brushtrainingsimulator.api.ApiService;
import com.example.clabuyakchai.brushtrainingsimulator.api.Client;
import com.example.clabuyakchai.brushtrainingsimulator.model.UserRegistration;
import com.example.clabuyakchai.brushtrainingsimulator.stateinternet.StateInternet;
import com.example.clabuyakchai.brushtrainingsimulator.validator.RegistrationValidator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Clabuyakchai on 04.05.2018.
 */

public class SignUpFragment extends Fragment {

    private TextView mUsername;
    private TextView mEmail;
    private TextView mPassword;
    private TextView mSignIn;

    private Button mJoin;

    private ImageView mBack;

    private FragmentTransaction mFragmentTransaction;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        mUsername = view.findViewById(R.id.signupUserName);
        mEmail = view.findViewById(R.id.signupEmail);
        mPassword = view.findViewById(R.id.signupPassword);
        mSignIn = view.findViewById(R.id.signinTextView);

        mJoin = view.findViewById(R.id.signupButton);

        mBack = view.findViewById(R.id.signupBack);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backSignIn();
            }
        });

        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backSignIn();
            }
        });

        mJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StateInternet.hasConnection(getActivity())){
                    UserRegistration user = new UserRegistration(mUsername.getText().toString(),
                            mEmail.getText().toString(),
                            mPassword.getText().toString());

                    String message = RegistrationValidator.userValidation(user);
                    if (message == null) {
                        ApiService service = Client.getApiService();
                        Call<ResponseBody> call = service.signup(user);

                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
                                    backSignIn();
                                } else {
                                    Toast.makeText(getActivity(), "Someone already has that username.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(getActivity(), "onFailure", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.error_internet_connection, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void backSignIn(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
        mFragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.fragment_container, new SignInFragment()).commit();
    }
}
