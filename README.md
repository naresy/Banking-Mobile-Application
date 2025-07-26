<title>Banking Application - Android</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      line-height: 1.6;
      margin: 20px;
      background-color: #f8f9fa;
      color: #333;
    }
    h1, h2 {
      color: #004085;
    }
    code {
      background-color: #e2e6ea;
      padding: 2px 4px;
      border-radius: 4px;
    }
    ul {
      margin-top: 0;
    }
    .section {
      margin-bottom: 30px;
    }
  </style>
</head>
<body>

  <h1>📱 Android Banking Application</h1>

  <div class="section">
    <p>This is a lightweight, secure Android Banking App built using Java. The app includes features like user registration, login with fingerprint support, account management, value export, and user verification through scanned input.</p>
  </div>

  <div class="section">
    <h2>🧱 Project Structure</h2>
    <ul>
      <li><strong>MainActivity.java</strong> - Entry point of the app</li>
      <li><strong>MyApplication.java</strong> - Base class for maintaining global app state</li>
      <li><strong>HttpParse.java</strong> - Handles server communication via HTTP</li>
      <li><strong>login_activity.java</strong> - Login screen with authentication</li>
      <li><strong>finger_print.java</strong> - Fingerprint authentication logic</li>
      <li><strong>account_fill.java</strong> - Account details input form</li>
      <li><strong>create_new.java</strong> - New account creation screen</li>
      <li><strong>export_value.java</strong> - Export data to external formats or reports</li>
      <li><strong>read_user.java</strong> - Reads user data from server/database</li>
      <li><strong>scanned.java</strong> - Handles input from QR or barcode scans</li>
      <li><strong>verification.java</strong> - User identity verification</li>
    </ul>
  </div>

  <div class="section">
    <h2>⚙️ Features</h2>
    <ul>
      <li>User login and secure authentication</li>
      <li>Fingerprint login (biometric authentication)</li>
      <li>Account creation and data management</li>
      <li>Data export functionality</li>
      <li>QR/Barcode scanning and user verification</li>
    </ul>
  </div>

  <div class="section">
    <h2>🚀 Getting Started</h2>
    <p>To run the project locally:</p>
    <ol>
      <li>Clone the repository</li>
      <li>Open the project in Android Studio</li>
      <li>Build and run on a physical Android device or emulator</li>
    </ol>
  </div>

  <div class="section">
    <h2>📁 Requirements</h2>
    <ul>
      <li>Android Studio (Arctic Fox or higher)</li>
      <li>Java 8+</li>
      <li>Minimum SDK: 21 (Android 5.0)</li>
    </ul>
  </div>

  <div class="section">
    <h2>🛡️ Security</h2>
    <p>Ensure secure API endpoints and use HTTPS for all network requests. Fingerprint data is managed using the Android BiometricPrompt API and never stored.</p>
  </div>

  <div class="section">
    <h2>📬 Contact</h2>
    <p>If you have any questions or suggestions, feel free to reach out to the project owner.</p>
  </div>

</body>
</html>
