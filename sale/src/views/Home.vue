<template>
  <div class="min-h-screen flex flex-col w-full">
    <!-- Header -->
    <header class="bg-white shadow-sm h-12 flex items-center">
      <div class="mx-auto w-full  flex items-center justify-center gap-8">
        <div class="flex items-center gap-2 mr-48">
          <div class="h-8 w-8 rounded-full flex items-center justify-center">
           <img src="../assets/img/logo.png" style="height:30px;" alt="" />
          </div>
          <span class="text-[#333333] text-[28px] font-['YouSheBiaoTiHei'] whitespace-nowrap">融销通</span>
        </div>
        
        <nav class="hidden md:flex space-x-8 mr-56 items-center">
          <router-link to="/home" class="min-w-[60px] text-center flex-shrink-0 text-gray-700 hover:border-b-2 hover:border-[#007029] text-[19px]" :class="{ 'border-b-2 border-[#007029]': $route.path === '/home' }">首页</router-link>
          <router-link to="/financinglist" class="min-w-[80px] text-center flex-shrink-0 text-gray-700 hover:border-b-2 hover:border-[#007029] text-[19px]" :class="{ 'border-b-2 border-[#007029]': $route.path === '/financinglist' }">融资服务</router-link>
          <router-link to="/expertassist" class="min-w-[80px] text-center flex-shrink-0 text-gray-700 hover:border-b-2 hover:border-[#007029] text-[19px]" :class="{ 'border-b-2 border-[#007029]': $route.path === '/expertassist' }">专家助力</router-link>
          <router-link to="/agripro" class="min-w-[80px] text-center flex-shrink-0 text-gray-700 hover:border-b-2 hover:border-[#007029] text-[19px]" :class="{ 'border-b-2 border-[#007029]': $route.path === '/agripro' }">农产品交易</router-link>
          <router-link v-if="$store.state.token!=''" to="/personal" class="min-w-[90px] text-center flex-shrink-0 text-gray-700 hover:border-b-2 hover:border-[#007029] text-[19px]" :class="{ 'border-b-2 border-[#007029]': $route.path.startsWith('/personal') }">个人信息</router-link>
          
        </nav>
        
        <div class="flex items-center gap-3">
          <div class="flex items-center gap-1" v-if="$store.state.token==''">
            <UserIcon class="h-4 w-4 text-gray-600" />
            <router-link to="/login" >
                <span class="text-[18px] text-gray-600 hover:text-[#007029]">登录</span>
            </router-link>
            <span class="text-[18px] text-gray-600">|</span>
          </div>
          
          <div class="flex items-center gap-1" v-if="$store.state.token==''">
            <router-link to="/register" >
                <span class="text-[18px] text-gray-600 hover:text-[#007029]">注册</span>
            </router-link>
          </div>
          <div class="flex items-center gap-1" v-if="$store.state.token!=''">
            <span class="text-[18px] text-gray-600 hover:text-[#007029]" @click="logout()">退出</span>
          </div>
        </div>
      </div>
    </header>

    <main class="flex-grow bg-white">
      <router-view></router-view>
    </main>

    <!-- Footer -->
    <footer class="bg-[#007029] text-white py-6">
      <div class="container mx-auto px-4">
        <div class="flex flex-col md:flex-row justify-between items-center">
          <div class="flex items-center gap-2 mb-4 md:mb-0">
            <div class="h-8 w-8 rounded-full bg-white flex items-center justify-center">
              <div class="h-8 w-8 rounded-full flex items-center justify-center">
           <img src="../assets/img/logobtm.png" class="h-8 w-8" alt="" />
          </div>
            </div>
            <span class="font-bold">融销通</span>
          </div>
          <div class="flex gap-6 text-sm">
            <a href="#" class="hover:underline">关于我们</a>
            <a href="#" class="hover:underline">联系方式</a>
            <a href="#" class="hover:underline">服务条款</a>
            <a href="#" class="hover:underline">隐私政策</a>
          </div>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { UserIcon } from 'lucide-vue-next';
import { ref,onMounted } from 'vue';
import riceImg from "@/assets/img/rice.png";
import { apiClient } from "../api/apiService.js";
import { ElMessage } from 'element-plus';
import { useStore } from 'vuex';
import { useRouter } from 'vue-router';
import { Base64 } from 'js-base64'; // 解构导入 Base64 类

const store = useStore();
const router = useRouter();

//退出
const logout = () => {
  store.commit('updateLoginUserNickname', "");
  store.commit('removeStorage', "");
  //跳转至首页
  router.push("/home").catch((err) => err);
};

</script>

<style>
@tailwind base;
@tailwind components;
@tailwind utilities;

@font-face {
  font-family: 'YouSheBiaoTiHei';
  src: url('../assets/font/YouSheBiaoTiHei-2.ttf') format('truetype');
  font-weight: normal;
  font-style: normal;
}
</style>
