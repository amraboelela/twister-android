//
//  NewAccountActivity.kt
//  Twister
//
//  Created by Amr Aboelela on 10/26/18.
//  Copyright © 2018 Twister Org. All rights reserved.
//

package net.twister.twister

class NewAccountActivity : NewAccountBaseActivity()/*
    @IBOutlet weak var confirmPasswordTextField: UITextField!
    @IBOutlet weak var userInfoHeightConstraint: NSLayoutConstraint!

    var originalUserInfoViewHeight = CGFloat(180)

    override func viewDidLoad() {
        super.viewDidLoad()
        //self.navigationItem.setHidesBackButton(true, animated:false)
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

    // MARK: - Delegates

    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        print("textFieldShouldReturn")
        if textField == self.usernameTextField {
            self.passwordTextField.becomeFirstResponder()
        } else if textField == self.passwordTextField {
            self.confirmPasswordTextField.becomeFirstResponder()
        } else if textField == self.confirmPasswordTextField {
            self.fullnameTextField.becomeFirstResponder()
        } else if textField == self.fullnameTextField {
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

    // MARK: - Actions

    @IBAction func submitButtonClicked(_ sender: Any) {
        print("submitButtonClicked")
        guard let usernameText = usernameTextField.text else {
            print("usernameTextField.text is nil")
            return
        }
        guard let passwordText = passwordTextField.text else {
            print("passwordTextField.text is nil")
            return
        }
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
        let validUsernameCharacterSet = CharacterSet.lowercaseLetters.union(CharacterSet(charactersIn: "0123456789_"))
        if usernameText.rangeOfCharacter(from: validUsernameCharacterSet.inverted) != nil || usernameText.count < 3 {
            showUsernameAlert(withMessage: "Please enter a valid username")
            return
        }
        if userAdapter.usernameExists(usernameText) {
            showUsernameAlreadyExistsAlert()
            return
        }
        if passwordText.rangeOfCharacter(from: CharacterSet.whitespacesAndNewlines) != nil || passwordText.count < 8 {
            let alertController = UIAlertController(title: nil, message: "Please enter a valid password", preferredStyle: .alert)
            let okAction = UIAlertAction(title: "OK", style: .cancel) { action in
                self.passwordTextField.text = ""
                self.passwordTextField.becomeFirstResponder()
            }
            alertController.addAction(okAction)
            self.navigationController?.present(alertController, animated: true, completion: nil)
            return
        }
        if let confirmPasswordText = confirmPasswordTextField.text, passwordText != confirmPasswordText {
            let alertController = UIAlertController(title: nil, message: "Confirm password mismatch", preferredStyle: .alert)
            let okAction = UIAlertAction(title: "OK", style: .cancel) { action in
                self.confirmPasswordTextField.text = ""
                self.confirmPasswordTextField.becomeFirstResponder()
            }
            alertController.addAction(okAction)
            self.navigationController?.present(alertController, animated: true, completion: nil)
            return
        }
        if let data = passwordText.data(using: .utf8) {
            do {
                if let encodedPassword = try data.encryptedWithSaltUsing(key: server.id) {
                    let encodedPasswordString = encodedPassword.hexEncodedString
                    print("encodedPasswordString: \(encodedPasswordString)")
                    UIApplication.shared.isNetworkActivityIndicatorVisible = true
                    serverProxy.newAccount(username: usernameText, password: encodedPasswordString, fullname: fullnameText, bio: bioText, location: locationText, url: urlText) { networkMessage, error in
                        DispatchQueue.main.async {
                            UIApplication.shared.isNetworkActivityIndicatorVisible = false
                            if let error = error {
                                if let networkMessageError = error as? NetworkMessageError {
                                    switch networkMessageError {
                                    case .alreadyExists:
                                        self.showUsernameAlreadyExistsAlert()
                                    default:
                                        self.showUsernameAlert(withMessage: "Something wrong happen")
                                    }
                                } else {
                                    self.showAlert(withMessage: "Connection error")
                                }
                            } else {
                                if var userprofile = networkMessage?.userprofile {
                                    userprofile[Username] = usernameText
                                    userAdapter.set(userprofile: userprofile, forUsername: usernameText)
                                } else {
                                    userAdapter.set(userprofile: [Username: usernameText, Fullname: fullnameText, Bio: bioText, Location: locationText, URLKey : urlText], forUsername: usernameText)
                                }
                                userAdapter.currentUsername = usernameText
                                self.backClicked(self)
                            }
                        }
                    }
                }
            } catch {
                print("passwordText error: \(error)")
            }
        }

    }

    // MARK: - Private methods
    */

