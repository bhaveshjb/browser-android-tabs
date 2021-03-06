// Copyright 2017 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef CHROME_BROWSER_CHROMEOS_NOTE_TAKING_CONTROLLER_CLIENT_H_
#define CHROME_BROWSER_CHROMEOS_NOTE_TAKING_CONTROLLER_CLIENT_H_

#include "ash/public/cpp/note_taking_client.h"
#include "base/macros.h"
#include "chrome/browser/chromeos/note_taking_helper.h"
#include "components/user_manager/user_manager.h"
#include "content/public/browser/notification_observer.h"
#include "content/public/browser/notification_registrar.h"

class Profile;

namespace chromeos {

class NoteTakingControllerClient
    : public ash::NoteTakingClient,
      public user_manager::UserManager::UserSessionStateObserver,
      public content::NotificationObserver {
 public:
  explicit NoteTakingControllerClient(NoteTakingHelper* helper);
  ~NoteTakingControllerClient() override;

  // ash::NoteTakingClient:
  bool CanCreateNote() override;
  void CreateNote() override;

  // user_manager::UserManager::UserSessionStateObserver:
  void ActiveUserChanged(const user_manager::User* active_user) override;

  // content::NotificationObserver:
  void Observe(int type,
               const content::NotificationSource& source,
               const content::NotificationDetails& details) override;

  void SetProfileForTesting(Profile* profile) { SetProfile(profile); }

 private:
  void SetProfile(Profile* profile);

  // Unowned pointer to the note taking helper.
  NoteTakingHelper* helper_;

  // Unowned pointer to the active profile.
  Profile* profile_ = nullptr;

  content::NotificationRegistrar registrar_;
  std::unique_ptr<user_manager::ScopedUserSessionStateObserver>
      session_state_observer_;

  DISALLOW_COPY_AND_ASSIGN(NoteTakingControllerClient);
};

}  // namespace chromeos

#endif  // CHROME_BROWSER_CHROMEOS_NOTE_TAKING_CONTROLLER_CLIENT_H_
