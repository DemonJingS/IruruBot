const canvas = document.getElementById('wheelCanvas');
const ctx = canvas.getContext('2d');

// 轮盘数据
const wheelData = [
    { label: 'Increase', value: 1, color: '#ff6b81' },
    { label: 'Decrease', value: -1, color: '#ff9e80' },
    { label: '2x', value: 2, color: '#ffcccb' },
    { label: '3x', value: 3, color: '#ffebcd' },
    { label: '5 minutes', value: 5, color: '#e8f5e9' },
    { label: '10 minutes', value: 10, color: '#bbdefb' },
    { label: '15 minutes', value: 15, color: '#c5cae9' },
    { label: '30 minutes', value: 30, color: '#e1bee7' }
];

// 画轮盘
function drawWheel() {
    const radius = Math.min(canvas.width, canvas.height) / 2 - 10;
    const centerX = canvas.width / 2;
    const centerY = canvas.height / 2;

    // 清除画布
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    // 画轮盘
    for (let i = 0; i < wheelData.length; i++) {
        const startAngle = (i * (Math.PI * 2)) / wheelData.length;
        const endAngle = ((i + 1) * (Math.PI * 2)) / wheelData.length;
        const color = wheelData[i].color;

        ctx.beginPath();
        ctx.moveTo(centerX, centerY);
        ctx.arc(centerX, centerY, radius, startAngle, endAngle, false);
        ctx.closePath();
        ctx.fillStyle = color;
        ctx.fill();

        // 画指针
        ctx.beginPath();
        ctx.moveTo(centerX, centerY);
        ctx.lineTo(centerX + radius * 0.9 * Math.cos(startAngle + (endAngle - startAngle) / 2), centerY + radius * 0.9 * Math.sin(startAngle + (endAngle - startAngle) / 2));
        ctx.stroke();
    }
}

// 旋转轮盘
function spinWheel() {
    let angle = 0;
    let speed = 0.1;

    function animate() {
        angle += speed;
        speed *= 0.995; // 减速

        ctx.clearRect(0, 0, canvas.width, canvas.height);
        drawWheel();

        if (speed > 0.001) {
            requestAnimationFrame(animate);
        } else {
            // 旋转结束后的处理
            const index = Math.floor((angle % (Math.PI * 2)) / ((Math.PI * 2) / wheelData.length));
            console.log(`Result: ${wheelData[index].label}`);
        }
    }

    animate();
}

document.getElementById('spinButton').addEventListener('click', spinWheel);

drawWheel();
