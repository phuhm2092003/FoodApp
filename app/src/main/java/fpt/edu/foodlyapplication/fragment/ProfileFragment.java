package fpt.edu.foodlyapplication.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import fpt.edu.foodlyapplication.view.ChangePasswordActivity;
import fpt.edu.foodlyapplication.MainActivity;
import fpt.edu.foodlyapplication.R;
import fpt.edu.foodlyapplication.view.UpdateInfoAccountActivity;
import fpt.edu.foodlyapplication.model.User;
import fpt.edu.foodlyapplication.utils.Server;
import fpt.edu.foodlyapplication.view.SignInActivity;

public class ProfileFragment extends Fragment {
    public static final String EXTRA_USER_EMAIL = "EmailUser";
    private static final String TAG = "ProfileFragment";
    public static final String SERVER_URL_GET_USER = Server.url_get_user_by_email;
    public static final String RESPONSE_USER_NOT_EXISTS = "User Not Exists";
    public static final String PARAM_EMAIL = "email";
    private TextView tvFullname, tvEmail;
    private MainActivity mainActivity;
    private ConstraintLayout itemAccoutInfo, itemChangePassword, itemLogout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mainActivity = (MainActivity) getActivity();
        initView(view);
        setListeners();
        setUserDetails();

        return view;
    }


    private void initView(View view) {
        tvFullname = (TextView) view.findViewById(R.id.tvFullName);
        tvEmail = (TextView) view.findViewById(R.id.tvEmail);
        itemAccoutInfo = (ConstraintLayout) view.findViewById(R.id.itemAccoutInfo);
        itemChangePassword = (ConstraintLayout) view.findViewById(R.id.itemChangePassword);
        itemLogout = (ConstraintLayout) view.findViewById(R.id.itemLogout);
    }

    private void setListeners() {
        itemAccoutInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), UpdateInfoAccountActivity.class);
                intent.putExtra(EXTRA_USER_EMAIL, mainActivity.getKeyUser());
                startActivity(intent);
            }
        });

        itemChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ChangePasswordActivity.class);
                intent.putExtra(EXTRA_USER_EMAIL, mainActivity.getKeyUser());
                startActivity(intent);
            }
        });

        itemLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(), SignInActivity.class));
            }
        });
    }

    private void setUserDetails() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest getUserRequest = new StringRequest(Request.Method.POST, SERVER_URL_GET_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Get response from sever
                if (response.equals(RESPONSE_USER_NOT_EXISTS)) {
                    Log.i(TAG, RESPONSE_USER_NOT_EXISTS);
                } else {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        User user = new User();
                        user.setEmail(jsonObject.getString("Email"));
                        user.setFullname(jsonObject.getString("FullName"));
                        user.setPassword(jsonObject.getString("Password"));
                        Log.i(TAG, user.toString());
                        // Set text
                        tvEmail.setText(user.getEmail());
                        tvFullname.setText(user.getFullname());

                    } catch (JSONException e) {
                        Log.e(TAG, e.toString());
                        throw new RuntimeException(e);
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Sever error:  " + error.toString());
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Add email to request body in sever
                HashMap<String, String> params = new HashMap<>();
                params.put(PARAM_EMAIL, mainActivity.getKeyUser());
                return params;
            }
        };
        requestQueue.add(getUserRequest);
    }


    @Override
    public void onResume() {
        setUserDetails();
        super.onResume();
    }
}