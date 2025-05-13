document.addEventListener('DOMContentLoaded', function() {
  const userIdInput = document.getElementById('userIdInput');
  const loginBtn = document.getElementById('loginBtn');
  const logoutBtn = document.getElementById('logoutBtn');
  const errorMessage = document.getElementById('error-message');
  const loginSection = document.getElementById('login-section');
  const logoutSection = document.getElementById('logout-section');
  const displayUserId = document.getElementById('displayUserId');

  checkAuthStatus();
  
  loginBtn.addEventListener('click', function() {
    const userId = userIdInput.value.trim();
    
    if (userId.length < 3) {
      showError("Code must be at least 3 characters");
      return;
    }

    chrome.storage.local.set({ userId: userId }, function() {
      if (chrome.runtime.lastError) {
        showError("Failed to save user ID");
        return;
      }
      showSuccess();
      checkAuthStatus();
    });
  });
  
  logoutBtn.addEventListener('click', function() {
    chrome.storage.local.remove('userId', function() {
      if (chrome.runtime.lastError) {
        console.error("Failed to remove user ID:", chrome.runtime.lastError);
      }
      checkAuthStatus();
      userIdInput.value = '';
    });
  });
  
  userIdInput.addEventListener('input', function() {
    hideError();
  });
  
  function checkAuthStatus() {
    chrome.storage.local.get(['userId'], function(result) {
      if (chrome.runtime.lastError) {
        console.error("Error reading storage:", chrome.runtime.lastError);
        return;
      }
      
      const userId = result.userId;
      
      if (userId) {
        loginSection.style.display = 'none';
        logoutSection.style.display = 'flex';
        displayUserId.textContent = userId;
      } else {
        loginSection.style.display = 'block';
        logoutSection.style.display = 'none';
      }
    });
  }

  function showError(message) {
    errorMessage.textContent = message;
    errorMessage.style.display = 'block';
    userIdInput.style.borderColor = '#e74c3c';
  }

  function hideError() {
    errorMessage.style.display = 'none';
    userIdInput.style.borderColor = '#ddd';
  }
  
  function showSuccess() {
  }
});