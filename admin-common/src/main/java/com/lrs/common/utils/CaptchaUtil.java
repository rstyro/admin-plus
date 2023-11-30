package com.lrs.common.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CaptchaUtil {
    private static final String[] CODE = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "P",
            "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

    /**
     * 生成验证码图片
     * @return obj[0]: 图片; obj[1]:字符串
     */
    public static Object[] CreateCode() {
        int imgW = 120;
        int imgH = 42;
        String result = "";
        ThreadLocalRandom random = ThreadLocalRandom.current();
        BufferedImage img = new BufferedImage(imgW, imgH, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = img.createGraphics();
        graphics.setFont(new Font("MicroSoft YaHei", Font.PLAIN, 30));

        // 绘制背景色
        graphics.setColor(generateRandomColor(230,20));
        graphics.fillRect(0, 0, imgW, imgH);

        // 绘制背景干扰线条
        for (int i = 0; i < 3; i++) {
            graphics.setColor(generateRandomColor(200,50));
            graphics.setStroke(new BasicStroke(2.0f));
            graphics.drawLine(random.nextInt(20), random.nextInt(imgH), random.nextInt(20) + 80, random.nextInt(imgH));
        }

        if (random.nextInt(100) >= 50) {
            // 绘制字符串验证码
            for (int i = 0; i < 4; i++) {
                String str = CODE[random.nextInt(CODE.length)];
                graphics.setColor(generateRandomColor(180,50));
                graphics.drawString(str, (i * 20) + 20, random.nextInt(4) + 30);
                result += str;
            }
        } else {
            // 绘制计算题验证码
            int is = random.nextInt(100);
            int num1 = random.nextInt(9) + 1;
            int num2 = random.nextInt(9) + 1;
            String operator = (is >= 50) ? "+" : "×";
            int answer = (operator.equals("+")) ? (num1 + num2) : (num1 * num2);

            // 绘制第一个值
            graphics.setColor(generateRandomColor(180, 50));
            graphics.drawString(String.valueOf(num1), 20, random.nextInt(4) + 30);

            // 符号
            graphics.setColor(generateRandomColor(180, 50));
            graphics.drawString(operator, 40, random.nextInt(4) + 30);

            // 第二个
            graphics.setColor(generateRandomColor(180, 50));
            graphics.drawString(String.valueOf(num2), 60, random.nextInt(4) + 30);

            // 等于
            graphics.setColor(generateRandomColor(180, 50));
            graphics.drawString("=", 80, random.nextInt(4) + 30);

            result = String.valueOf(answer);
        }

        // 绘制前景干扰线条
        for (int i = 0; i < 3; i++) {
            graphics.setColor(generateRandomColor(200,50));
            graphics.setStroke(new BasicStroke(1.0f));
            graphics.drawLine(0, random.nextInt(imgH), imgW, random.nextInt(imgH));
        }
        return new Object[]{img, result};
    }

    /**
     * 生成随机RGB颜色
     * @param baseNum 基础颜色值
     * @param bound 叠加的 随机数
     * @return Color
     */
    public static Color generateRandomColor(int baseNum,int bound){
        int r, g, b;
        ThreadLocalRandom random = ThreadLocalRandom.current();
        r = random.nextInt(bound) + baseNum;
        g = random.nextInt(bound) + baseNum;
        b = random.nextInt(bound) + baseNum;
        return new Color(r, g, b);
    }

}
