//
//  ReplyPostActivity.kt
//  Twister
//
//  Created by Amr Aboelela on 10/26/18.
//  Copyright © 2018 Twister Org. All rights reserved.
//

package net.twister.twister

class ReplyPostActivity : PostActivity()/*
    @IBOutlet weak var postTextView: UITextView!
    @IBOutlet weak var scrollViewBottomConstraint: NSLayoutConstraint!
    @IBOutlet weak var contentViewWidthConstraint: NSLayoutConstraint!
    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var contentView: UIView!

    var keyboardWillShowObserver: NSObjectProtocol?
    var keyboardWillHideObserver: NSObjectProtocol?
    var deviceOrientationDidChangeObserver: NSObjectProtocol?

    override func viewDidLoad() {
        super.viewDidLoad()
        postTextView.becomeFirstResponder()
        postTextView.showBorder()
        print("postKey: \(postKey)")
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        keyboardWillShowObserver = notificationCenter.addObserver(forName: .UIKeyboardWillShow, object: nil, queue: nil) { [weak self] notification in
            DispatchQueue.main.async {
                if let keyboardSize = (notification.userInfo?[UIKeyboardFrameEndUserInfoKey] as? NSValue)?.cgRectValue {
                    UIView.animate(withDuration: 0.25) {
                        self?.scrollViewBottomConstraint.constant = keyboardSize.height
                        self?.updateUI()
                    }
                }
            }
        }
        keyboardWillHideObserver = notificationCenter.addObserver(forName: .UIKeyboardWillHide, object: nil, queue: nil) { [weak self] notification in
            DispatchQueue.main.async {
                UIView.animate(withDuration: 0.25) {
                    self?.scrollViewBottomConstraint.constant = 0
                    self?.updateUI()
                }
            }
        }
        deviceOrientationDidChangeObserver = notificationCenter.addObserver(forName: .UIDeviceOrientationDidChange, object: nil, queue: nil) { [weak self] notification in
            DispatchQueue.main.async {
                self?.updateUI()
            }
        }
    }

    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        if let keyboardWillShowObserver = keyboardWillShowObserver {
            notificationCenter.removeObserver(keyboardWillShowObserver)
        }
        if let keyboardWillHideObserver = keyboardWillHideObserver {
            notificationCenter.removeObserver(keyboardWillHideObserver)
        }
        if let deviceOrientationDidChangeObserver = deviceOrientationDidChangeObserver {
            notificationCenter.removeObserver(deviceOrientationDidChangeObserver)
        }
    }

    deinit {
        print("deinit: \(self)")
    }

    // MARK: - Data handling

    // MARK: - User interface

    func updateUI() {
        self.contentViewWidthConstraint.constant = self.view.frame.size.width
        self.scrollView.contentSize = CGSize(width: self.contentViewWidthConstraint.constant , height: self.contentView.frame.size.height)
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.1) {
            let postTextViewY = self.postTextView.frame.origin.y
            let requiredVisibleHeight = self.contentView.frame.size.height - postTextViewY
            var offset = CGFloat(0)
            if self.scrollView.frame.size.height < self.contentView.frame.size.height {
                let diff = self.scrollView.frame.size.height - requiredVisibleHeight
                if diff > 0 {
                    offset = postTextViewY - diff
                } else {
                    offset = postTextViewY - 8
                }
            }
            UIView.animate(withDuration: 0.25) {
                self.scrollView.contentOffset = CGPoint(x: 0, y: offset)
            }
        }
    }

    // MARK: - Delegates

    override func textViewTapped(sender: PostTextView) {
        print("textViewTapped")
    }

    // MARK: - Actions

    @IBAction func submitButtonClicked(_ sender: Any) {
        print("submitButtonClicked")
        guard let postText = postTextView.text else {
            print("postTextView.text is nil")
            return
        }
        guard let currentUsername = userAdapter.currentUsername else {
            print("currentUsername is nil")
            return
        }
        if postText.isEmpty {
            showAlert(withMessage: "Please enter the post")
            return
        }
        UIApplication.shared.isNetworkActivityIndicatorVisible = true
        let replyPost = postAdapter.post(atKey: postKey)
        let replyID = postAdapter.id(ofPost: replyPost)
        let replyUsername = postAdapter.username(ofPost: replyPost)
        serverProxy.newPost(message: postText, username: currentUsername, replyUsername: replyUsername, replyID: replyID) { networkMessage, error in
            DispatchQueue.main.async {
                UIApplication.shared.isNetworkActivityIndicatorVisible = false
                if let error = error {
                    print("error: \(error)")
                    self.showAlert(withMessage: "Something wrong happen")
                } else if let networkMessage = networkMessage, let status = networkMessage.status, status == NetworkMessageStatus.success.rawValue {
                    self.backClicked(self)
                    if let newPostDic = networkMessage.newPost {
                        postAdapter.save(newPost: newPostDic)
                    }
                }
            }
        }
    }

    // MARK: - Private methods
    */

