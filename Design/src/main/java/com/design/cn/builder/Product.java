package com.design.cn.builder;

/**
 * create by Dale
 * create on 2019/5/23
 * description:
 */
public class Product {

    private String cpu;
    private String memory;
    private String dispalyCard;

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getDispalyCard() {
        return dispalyCard;
    }

    public void setDispalyCard(String dispalyCard) {
        this.dispalyCard = dispalyCard;
    }

    @Override
    public String toString() {
        return "Product{" +
                "cpu='" + cpu + '\'' +
                ", memory='" + memory + '\'' +
                ", dispalyCard='" + dispalyCard + '\'' +
                '}';
    }

    public static class Builder {
        private String cpu;
        private String memory;
        private String dispalyCard;

        public Builder buildCpu(String cpu) {
            this.cpu = cpu;
            return this;
        }

        public Builder buildMemory(String memory) {
            this.memory = memory;
            return this;
        }

        public Builder buildDispalyCard(String dispalyCard) {
            this.dispalyCard = dispalyCard;
            return this;
        }

        public Product createProduct() {
            Product product = new Product();
            product.setCpu(cpu);
            product.setMemory(memory);
            product.setDispalyCard(dispalyCard);
            return product;
        }
    }
}
