package com.example.sparrow.redis.demo;

import org.springframework.core.io.ClassPathResource;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 这个类是用来随机生成邮箱地址的，
 * @numEmails 生成的邮箱条数
 * @currentPath 当前工作区目录
 * @filePath 生成的目标文件
 */
public class GenerateBlacklist {

    private static int numEmails = 100000; // 生成10万个随机邮箱

    public static void main(String[] args) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("blacklist.txt");
        File file = classPathResource.getFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.getPath()))) {
            for (int i = 0; i < numEmails; i++) {
                writer.write(generateRandomEmail());
                writer.newLine();
            }
            System.out.println("Generated " + numEmails + " random emails in " + file.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static List<String> generateRandomEmailList(int size){
        List<String> emailList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            emailList.add(generateRandomEmail());
        }
        return emailList;
    }

    /**
     * 生成的邮箱范围
     * @return
     */
    private static String generateRandomEmail() {
        String[] domains = {"example.com", "test.com", "mail.com", "domain.com"};
        String alphabet = "abcdefghijklmnopqrstuvwxyz1234567890";
        Random random = new Random();

        StringBuilder localPart = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            localPart.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }

        String domain = domains[random.nextInt(domains.length)];
        return localPart + "@" + domain;
    }
}
