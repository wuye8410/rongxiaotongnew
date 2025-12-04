<template>
  <section class="py-6 bg-white border-t-2 border-gray-300">
    <div class="flex container w-2/3 mx-auto h-full">
    <div class="w-64 bg-white shadow-md">
        <user-nav></user-nav>
        </div>
        <div class="flex-1 p-4 shadow-md">
          <router-view></router-view>
        </div>
    </div>
  </section>
</template>


<script setup>
import UserNav from "../components/PersonalInfoNav.vue";
</script>

<style scoped>
/* 可以添加额外的样式 */
</style>