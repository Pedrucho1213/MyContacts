// Generated by view binder compiler. Do not edit!
package com.example.mycontacts.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.mycontacts.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityListContactsBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final BottomNavigationView bottomNavigation;

  @NonNull
  public final RecyclerView contactsRv;

  @NonNull
  public final ExtendedFloatingActionButton newContactBtn;

  @NonNull
  public final SearchView searchBar;

  @NonNull
  public final AppBarLayout searchContainer;

  private ActivityListContactsBinding(@NonNull ConstraintLayout rootView,
      @NonNull BottomNavigationView bottomNavigation, @NonNull RecyclerView contactsRv,
      @NonNull ExtendedFloatingActionButton newContactBtn, @NonNull SearchView searchBar,
      @NonNull AppBarLayout searchContainer) {
    this.rootView = rootView;
    this.bottomNavigation = bottomNavigation;
    this.contactsRv = contactsRv;
    this.newContactBtn = newContactBtn;
    this.searchBar = searchBar;
    this.searchContainer = searchContainer;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityListContactsBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityListContactsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_list_contacts, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityListContactsBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.bottom_navigation;
      BottomNavigationView bottomNavigation = ViewBindings.findChildViewById(rootView, id);
      if (bottomNavigation == null) {
        break missingId;
      }

      id = R.id.contacts_rv;
      RecyclerView contactsRv = ViewBindings.findChildViewById(rootView, id);
      if (contactsRv == null) {
        break missingId;
      }

      id = R.id.new_contact_btn;
      ExtendedFloatingActionButton newContactBtn = ViewBindings.findChildViewById(rootView, id);
      if (newContactBtn == null) {
        break missingId;
      }

      id = R.id.search_bar;
      SearchView searchBar = ViewBindings.findChildViewById(rootView, id);
      if (searchBar == null) {
        break missingId;
      }

      id = R.id.search_container;
      AppBarLayout searchContainer = ViewBindings.findChildViewById(rootView, id);
      if (searchContainer == null) {
        break missingId;
      }

      return new ActivityListContactsBinding((ConstraintLayout) rootView, bottomNavigation,
          contactsRv, newContactBtn, searchBar, searchContainer);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
