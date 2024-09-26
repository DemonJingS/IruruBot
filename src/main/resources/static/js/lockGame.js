// 游戏模式按钮点击事件
document.getElementById('gameMode').addEventListener('click', function() {
    // 隐藏模式选择
    document.getElementById('modeSelection').classList.add('hidden');
    // 显示难度选择
    document.getElementById('difficultySelection').classList.remove('hidden');
});

// 倒计时模式按钮点击事件
document.getElementById('timerMode').addEventListener('click', function() {
    // 隐藏模式选择
    document.getElementById('modeSelection').classList.add('hidden');
    // 显示倒计时输入框
    document.getElementById('timerSelection').classList.remove('hidden');
});

// 选择难度按钮的点击事件
const difficultyButtons = document.querySelectorAll('.difficulty-button');
let selectedDifficulty = null;

difficultyButtons.forEach(button => {
    button.addEventListener('click', function() {
        // 重置所有按钮
        difficultyButtons.forEach(btn => btn.classList.remove('selected'));
        // 选中当前按钮
        this.classList.add('selected');
        selectedDifficulty = this.dataset.difficulty;

        // 激活开始挑战按钮
        document.querySelector('.start-button').classList.remove('disabled');
    });
});


// 监听输入框变化，判断是否启用开始按钮
document.getElementById('timerInput').addEventListener('input', function() {
    const startButton = document.querySelector('#timerSelection .start-button');
    if (this.value.trim() !== '') {
        startButton.classList.remove('disabled');
    } else {
        startButton.classList.add('disabled');
    }
});


