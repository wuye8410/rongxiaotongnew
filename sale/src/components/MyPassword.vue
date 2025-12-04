<template>
  <!-- 主内容区 -->
  <div class="max-w-2xl mx-auto pr-48">

    <!-- 修改密码 -->
    <div class="space-y-6">
      <div class="space-y-4 max-w-md mx-auto">
        <div class="space-y-2">
          <label class="block text-gray-600">当前密码</label>
          <input
            type="password"
            v-model="passwordForm.currentPassword"
            class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-1 focus:ring-green-500"
            placeholder="请输入当前密码"
          />
        </div>

        <div class="space-y-2">
          <label class="block text-gray-600">新密码</label>
          <input
            type="password"
            v-model="passwordForm.newPassword"
            class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-1 focus:ring-green-500"
            placeholder="请输入新密码"
          />
          <p class="text-xs text-gray-500">
            密码必须以字母开头，长度在6-18之间，只能包含英文字符、数字和下划线
          </p>
        </div>

        <div class="space-y-2">
          <label class="block text-gray-600">确认新密码</label>
          <input
            type="password"
            v-model="passwordForm.confirmPassword"
            class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-1 focus:ring-green-500"
            placeholder="请再次输入新密码"
          />
        </div>

        <div class="pt-4">
          <button
            @click="changePassword"
            class="w-full bg-green-700 hover:bg-green-800 text-white font-medium py-2 px-4 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500 focus:ring-offset-2"
          >
            确认修改
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from "vue";
import { apiClient } from "../api/apiService.js";
import { ElMessage } from "element-plus";

// 菜单展开状态
const expandedMenus = reactive({
  myInfo: true,
  myPosts: false,
});

// 切换菜单展开状态
const toggleMenu = (menu) => {
  expandedMenus[menu] = !expandedMenus[menu];
};



// 密码修改表单
const passwordForm = reactive({
  currentPassword: "",
  newPassword: "",
  confirmPassword: "",
});

// 修改密码
const changePassword = async() => {
  // 简单验证
  if (!passwordForm.currentPassword) {
    alert("请输入当前密码");
    return;
  }

  if (!passwordForm.newPassword) {
    alert("请输入新密码");
    return;
  }

  if (passwordForm.newPassword !== passwordForm.confirmPassword) {
    alert("两次输入的新密码不一致");
    return;
  }

  // 密码规则验证
  const passwordRegex = /^[a-zA-Z][a-zA-Z0-9_]{5,17}$/;
  if (!passwordRegex.test(passwordForm.newPassword)) {
    alert("密码必须以字母开头，长度在6-18之间，只能包含英文字符、数字和下划线");
    return;
  }

  // 模拟密码修改成功
 try{
    const param = {
      oldPassword: passwordForm.currentPassword,
      newPassword: passwordForm.newPassword,
    };



      // 添加新地址
      const response = await apiClient.post(`/user/loginUpdatePassword`, param, {
      headers: {
        "Content-Type": "application/json",
        Authorization: window.localStorage.token,
      },
      });

      if (response.flag) {
        ElMessage.success("密码修改成功。");
      } else {
        ElMessage.error(response.data);
      }

    // 重置表单
    passwordForm.confirmPassword='';
    passwordForm.currentPassword='';
    passwordForm.newPassword='';

  }  catch (error) {
    console.error('咨询失败，请重试:', error);
  }

  // 重置表单
  passwordForm.currentPassword = "";
  passwordForm.newPassword = "";
  passwordForm.confirmPassword = "";
};

</script>

<style scoped>
/* 可以添加额外的样式 */
</style>