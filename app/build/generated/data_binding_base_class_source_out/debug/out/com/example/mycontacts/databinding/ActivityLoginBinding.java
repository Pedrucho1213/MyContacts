// Generated by view binder compiler. Do not edit!
package com.example.mycontacts.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.mycontacts.R;
import com.google.android.material.textfield.TextInputLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityLoginBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextInputLayout emailTxt;

  @NonNull
  public final Button googleBtn;

  @NonNull
  public final Button loginBtn;

  @NonNull
  public final TextInputLayout passTxt;

  @NonNull
  public final Button registerBtn;

  @NonNull
  public final ConstraintLayout rootLayout;

  private ActivityLoginBinding(@NonNull ConstraintLayout rootView,
      @NonNull TextInputLayout emailTxt, @NonNull Button googleBtn, @NonNull Button loginBtn,
      @NonNull TextInputLayout passTxt, @NonNull Button registerBtn,
      @NonNull ConstraintLayout rootLayout) {
    this.rootView = rootView;
    this.emailTxt = emailTxt;
    this.googleBtn = googleBtn;
    this.loginBtn = loginBtn;
    this.passTxt = passTxt;
    this.registerBtn = registerBtn;
    this.rootLayout = rootLayout;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityLoginBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityLoginBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_login, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityLoginBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.email_txt;
      TextInputLayout emailTxt = ViewBindings.findChildViewById(rootView, id);
      if (emailTxt == null) {
        break missingId;
      }

      id = R.id.google_btn;
      Button googleBtn = ViewBindings.findChildViewById(rootView, id);
      if (googleBtn == null) {
        break missingId;
      }

      id = R.id.login_btn;
      Button loginBtn = ViewBindings.findChildViewById(rootView, id);
      if (loginBtn == null) {
        break missingId;
      }

      id = R.id.pass_txt;
      TextInputLayout passTxt = ViewBindings.findChildViewById(rootView, id);
      if (passTxt == null) {
        break missingId;
      }

      id = R.id.register_btn;
      Button registerBtn = ViewBindings.findChildViewById(rootView, id);
      if (registerBtn == null) {
        break missingId;
      }

      ConstraintLayout rootLayout = (ConstraintLayout) rootView;

      return new ActivityLoginBinding((ConstraintLayout) rootView, emailTxt, googleBtn, loginBtn,
          passTxt, registerBtn, rootLayout);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
