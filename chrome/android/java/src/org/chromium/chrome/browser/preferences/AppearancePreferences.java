/* Copyright (c) 2019 The Brave Authors. All rights reserved.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.chromium.chrome.browser.preferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;

import org.chromium.base.ContextUtils;
import org.chromium.base.Log;
import org.chromium.chrome.R;
import org.chromium.chrome.browser.preferences.BravePreferenceFragment;
import org.chromium.chrome.browser.BraveRewardsNativeWorker;
import org.chromium.chrome.browser.BraveRewardsObserver;
import org.chromium.chrome.browser.ChromeFeatureList;
import org.chromium.chrome.browser.accessibility.FontSizePrefs;
import org.chromium.chrome.browser.accessibility.FontSizePrefs.FontSizePrefsObserver;
import org.chromium.chrome.browser.preferences.PrefServiceBridge;
import org.chromium.chrome.browser.RestartWorker;
import org.chromium.chrome.browser.util.AccessibilityUtil;
import org.chromium.ui.base.DeviceFormFactor;

/**
 * Fragment to keep track of all the display related preferences.
 */
public class AppearancePreferences extends BravePreferenceFragment
        implements OnPreferenceChangeListener, BraveRewardsObserver {

    static final String PREF_HIDE_BRAVE_ICON = "hide_brave_rewards_icon";
    private BraveRewardsNativeWorker mBraveRewardsNativeWorker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.prefs_appearance);
        PreferenceUtils.addPreferencesFromResource(this, R.xml.appearance_preferences);
        if (!ChromeFeatureList.isEnabled(ChromeFeatureList.BRAVE_REWARDS) 
              || PrefServiceBridge.getInstance().isSafetynetCheckFailed()) {
            removePreferenceIfPresent(PREF_HIDE_BRAVE_ICON);
        }
        boolean isTablet = DeviceFormFactor.isNonMultiDisplayContextOnTablet(ContextUtils.getApplicationContext());
        if (isTablet) {
            removePreferenceIfPresent(ChromePreferenceManager.BOTTOM_TOOLBAR_ENABLED_KEY);
        }
    }

    private void removePreferenceIfPresent(String key) {
        Preference preference = getPreferenceScreen().findPreference(key);
        if (preference != null) getPreferenceScreen().removePreference(preference);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Preference hideBraveIconBlockPref = findPreference(PREF_HIDE_BRAVE_ICON);
        if (hideBraveIconBlockPref != null) {
            hideBraveIconBlockPref.setEnabled(false);
            hideBraveIconBlockPref.setOnPreferenceChangeListener(this);
        }
        Preference enableBottomToolbar = findPreference(ChromePreferenceManager.BOTTOM_TOOLBAR_ENABLED_KEY);
        if (enableBottomToolbar != null) {
            enableBottomToolbar.setOnPreferenceChangeListener(this);
            if (enableBottomToolbar instanceof ChromeSwitchPreference) {
              boolean isTablet = DeviceFormFactor.isNonMultiDisplayContextOnTablet(ContextUtils.getApplicationContext());
              ((ChromeSwitchPreference)enableBottomToolbar).setChecked(!isTablet && ChromePreferenceManager.getInstance().isBottomToolbarEnabled());
            }
        }
    }

    @Override
    public void onStart() {
        mBraveRewardsNativeWorker = BraveRewardsNativeWorker.getInstance();
        if (mBraveRewardsNativeWorker != null) {
          mBraveRewardsNativeWorker.AddObserver(this);
        }
        mBraveRewardsNativeWorker.GetRewardsMainEnabled();
        super.onStart();
    }

    @Override
    public void onStop() {
        if (mBraveRewardsNativeWorker != null) {
          mBraveRewardsNativeWorker.RemoveObserver(this);
        }
        super.onStop();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (PREF_HIDE_BRAVE_ICON.equals(preference.getKey())) {
            SharedPreferences sharedPreferences = ContextUtils.getAppSharedPreferences();
            SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
            sharedPreferencesEditor.putBoolean(PREF_HIDE_BRAVE_ICON, (boolean)newValue);
            sharedPreferencesEditor.apply();
            RestartWorker.AskForRelaunch(this.getActivity());
        } else if (ChromePreferenceManager.BOTTOM_TOOLBAR_ENABLED_KEY.equals(preference.getKey())) {
            SharedPreferences prefs = ContextUtils.getAppSharedPreferences();
            Boolean originalStatus = ChromePreferenceManager.getInstance().isBottomToolbarEnabled();
            prefs.edit().putBoolean(ChromePreferenceManager.BOTTOM_TOOLBAR_ENABLED_KEY, !originalStatus).apply();
            RestartWorker.AskForRelaunch(getActivity());
        }
        return true;
    }

    @Override
    public void OnWalletInitialized(int error_code) {}

    @Override
    public void OnWalletProperties(int error_code) {}

    @Override
    public void OnPublisherInfo(int tabId) {}

    @Override
    public void OnGetCurrentBalanceReport(String[] report) {}

    @Override
    public void OnNotificationAdded(String id, int type, long timestamp,
          String[] args) {}

    @Override
    public void OnNotificationsCount(int count) {}

    @Override
    public void OnGetLatestNotification(String id, int type, long timestamp,
              String[] args) {}

    @Override
    public void OnNotificationDeleted(String id) {}

    @Override
    public void OnIsWalletCreated(boolean created) {}

    @Override
    public void OnGetPendingContributionsTotal(double amount) {}

    @Override
    public void OnGetRewardsMainEnabled(boolean enabled) {
        ChromeSwitchPreference hideBraveIconBlockPref = (ChromeSwitchPreference)findPreference(PREF_HIDE_BRAVE_ICON);
        if (hideBraveIconBlockPref != null) {
          hideBraveIconBlockPref.setEnabled(!enabled);
          if (enabled) {
            hideBraveIconBlockPref.setChecked(false);
          }
        }
    }

    @Override
    public void OnGetAutoContributeProps() {}

    @Override
    public void OnGetReconcileStamp(long timestamp) {}

    @Override
    public void OnRecurringDonationUpdated() {}

    @Override
    public void OnResetTheWholeState(boolean success) {}

    @Override
    public void OnRewardsMainEnabled(boolean enabled) {}
}
