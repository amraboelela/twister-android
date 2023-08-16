//
//  MyAccountActivity.kt
//  Twister
//
//  Created by Amr Aboelela on 10/26/18.
//  Copyright © 2018 Twister Org. All rights reserved.
//

package net.twister.twister

class MyAccountActivity : AccountActivity()/*
    @IBOutlet weak var usernameLabel: UILabel!
    @IBOutlet weak var myPostsButton: UIButton!
    @IBOutlet weak var followingButton: UIButton!
    @IBOutlet weak var mentionsButton: UIButton!
    @IBOutlet weak var showPrivateKeyButton: UIButton!

    override func viewDidLoad() {
        super.viewDidLoad()
        guard let currentUsername = userAdapter.currentUsername else {
            print("currentUsername is nil")
            return
        }
        usernameLabel.text = "Username: \(currentUsername)"

        for buttonID in 1...5 {
            if let subview = (self.contentView.subviews.filter{$0.tag == buttonID}).first, let button = subview as? UIButton {
                button.layer.cornerRadius = button.frame.size.height / 2.0
                button.layer.borderWidth = 1.0
                button.layer.borderColor = skyColor.cgColor
                button.setTitleColor(skyColor, for: .normal)
            }
        }
        clientDatabase.updateUserProfile(withUsername: currentUsername) { error in
            if let error = error {
                print("updateUserProfile error: \(error)")
            } else {
                DispatchQueue.main.async {
                    if let currentUser = userAdapter.user(forUsername: currentUsername) {
                        if let privateKey = currentUser[PrivateKey] as? String {
                            self.privateKeyTextField.text = privateKey
                        }
                        if let fullname = currentUser[Fullname] as? String {
                            self.fullnameTextField.text = fullname
                        }
                        if let bio = currentUser[Bio] as? String {
                            self.bioTextField.text = bio
                        }
                        if let location = currentUser[Location] as? String {
                            self.locationTextField.text = location
                        }
                        if let url = currentUser[URLKey] as? String {
                            self.urlTextField.text = url
                        }
                    }
                }
            }
        }
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
    }

    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
    }

    deinit {
        print("deinit: \(self)")
    }

    // MARK: - Data handling

    override func updateData() {
        super.updateData()
    }

    // MARK: - Delegates

    public func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        print("textFieldShouldReturn")
        if textField == self.fullnameTextField {
            self.bioTextField.becomeFirstResponder()
        } else if textField == self.bioTextField {
            self.locationTextField.becomeFirstResponder()
        } else if textField == self.locationTextField {
            self.urlTextField.becomeFirstResponder()
        } else if textField == self.urlTextField {
            self.urlTextField.resignFirstResponder()
        }
        return true
    }

    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        return textField != self.privateKeyTextField
    }

    // MARK: - Actions

    @IBAction func updateButtonClicked(_ sender: Any) {
        print("updateButtonClicked")
        guard let fullnameText = fullnameTextField.text else {
            print("fullnameTextField.text is nil")
            return
        }
        guard let bioText = bioTextField.text else {
            print("bioTextField.text is nil")
            return
        }
        guard let locationText = locationTextField.text else {
            print("locationTextField.text is nil")
            return
        }
        guard let urlText = urlTextField.text else {
            print("urlTextField.text is nil")
            return
        }
        guard let currentUsername = userAdapter.currentUsername else {
            print("currentUsername is nil")
            return
        }
        touchIDVerify { error in
            if error == nil {
                print("touchIDVerify success")
                serverProxy.updateAccount(username: currentUsername, fullname: fullnameText, bio: bioText, location: locationText, url: urlText) { networkMessage, error in
                    DispatchQueue.main.async {
                        if error != nil {
                            self.showAlert(withMessage: "Failed to update")
                        } else {
                            if var currentUser = userAdapter.user(forUsername: currentUsername) {
                                currentUser[Fullname] = fullnameText
                                currentUser[Bio] = bioText
                                currentUser[Location] = locationText
                                currentUser[URLKey] = urlText
                                userAdapter.set(userprofile: currentUser, forUsername: currentUsername)
                            }
                            self.backClicked(self)
                        }
                    }
                }
            }
        }
    }

    @IBAction func showPrivateKeyClicked(_ sender: Any) {
        print("showPrivateKeyClicked")
        touchIDVerify { error in
            if error == nil {
                DispatchQueue.main.async {
                    print("touchIDVerify success")
                    self.privateKeyTextField.isHidden = false
                    self.showPrivateKeyButton.isHidden = true
                }
            }
        }
    }

    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        guard let currentUsername = userAdapter.currentUsername else {
            print("currentUsername is nil")
            return
        }
        if let vc = segue.destination as? UserPostsActivity {
            vc.username = currentUsername
        }
    }

    // MARK: - Private methods

    func touchIDVerify(callback: @escaping ErrorCallback) {
        //Create Local Authentication Context
        let authenticationContext = LAContext()
        var error:NSError?
        guard authenticationContext.canEvaluatePolicy(.deviceOwnerAuthenticationWithBiometrics, error: &error) else {
            print("No Biometric Sensor Has Been Detected. This device does not support Touch Id.")
            return
        }
        authenticationContext.evaluatePolicy(LAPolicy.deviceOwnerAuthenticationWithBiometrics, localizedReason: "Only device owner is allowed", reply: { success, error in
            if success {
                print("Fingerprint recognized. You are a device owner!")
            } else {
                // Check if there is an error
                if let errorObj = error {
                    print("Error took place. \(errorObj.localizedDescription)")
                }
            }
            callback(error)
        })
    }*/
