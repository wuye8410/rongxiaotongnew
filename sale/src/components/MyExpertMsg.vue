<template>
  <!-- 主内容区 -->
  <div class="max-w-2xl mx-auto pr-48">
    <!-- 专家基本信息 -->

    <div class="space-y-4">
      <div class="flex items-center">
        <label class="w-20 text-right mr-4 text-gray-600">真实姓名:</label>
        <input
          type="text"
          v-model="realName"
          class="flex-1 border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-1 focus:ring-green-500"
        />
      </div>
      <div class="flex items-center">
        <label class="w-20 text-right mr-4 text-gray-600">手机号:</label>
        <input
          type="text"
          v-model="phone"
          class="flex-1 border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-1 focus:ring-green-500"
        />
      </div>

      <div class="flex items-center">
        <label class="w-20 text-right mr-4 text-gray-600">从事专业：</label>
        <input
          type="text"
          v-model="profession"
          class="flex-1 border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-1 focus:ring-green-500"
        />
      </div>

      <div class="flex items-center">
        <label class="w-20 text-right mr-4 text-gray-600">职位:</label>
        <input
          type="text"
          v-model="position"
          class="flex-1 border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-1 focus:ring-green-500"
        />
      </div>

       <div class="flex items-center">
        <label class="w-20 text-right mr-4 text-gray-600">所属单位:</label>
        <input
          type="text"
          v-model="belong"
          class="flex-1 border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-1 focus:ring-green-500"
        />
      </div>

      <!-- 保存按钮 -->
      <div class="flex justify-center mt-8">
        <button
          @click="saveProfile"
          class="bg-green-700 hover:bg-green-800 text-white font-medium py-2 px-12 rounded-full focus:outline-none focus:ring-2 focus:ring-green-500 focus:ring-offset-2"
        >
          保存
        </button>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, reactive } from "vue";
import { onMounted } from "vue";
import { UserIcon } from "lucide-vue-next";
import { defineComponent } from "vue";
import { apiClient } from "../api/apiService.js";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import { useStore } from "vuex";

import { PlusIcon } from "lucide-vue-next";
const store = useStore();
// 菜单展开状态
const expandedMenus = reactive({
  myInfo: true,
  myPosts: false,
});

// 切换菜单展开状态
const toggleMenu = (menu) => {
  expandedMenus[menu] = !expandedMenus[menu];
};

// 基本信息表单数据
const userName = ref("");
const realName = ref("");
const phone = ref("");
const profession = ref("");
const position = ref("");
const belong = ref("");

onMounted(async () => {
  //检索专家信息
  selectExpMsg();
});

// 查询专家信息接口
const selectExpMsg = async () => {
  try {
    const response = await apiClient.get("/user/searchexpert", {
      headers: {
        Authorization: window.localStorage.token,
      },
    });
    console.log("请求成功", response.data);
    if (response.flag == true) {
      userName.value = response.data.userName;
      realName.value = response.data.realName;
      phone.value = response.data.phone;
      profession.value = response.data.profession;
      position.value = response.data.position;
      belong.value = response.data.belong;
      return;
    } else {
      console.error("请求失败", error);
    }
  } catch (error) {
    console.error("请求失败", error);
    throw error;
  }
};

// 保存个人信息
const saveProfile = async () => {
  try {
    const param = ref({
      userName:userName.value,
      realName:realName.value,
      phone:phone.value,
      profession:profession.value,
      position:position.value,
      belong:belong.value,
    });
    //增加咨询
    const response = await apiClient.post(
      `/user/addUpdexpert`,
      param.value,
      {
        headers: {
          "Content-Type": "application/json",
          Authorization: window.localStorage.token,
        },
      }
    );

    if (response.flag) {
      ElMessage.success("修改专家信息成功。");
    } else {
      ElMessage.error("修改专家信息失败，请重试。");
    }
  } catch (error) {
    console.error("修改专家信息失败，请重试：", error);
  }
};

</script>

<style scoped>
/* 可以添加额外的样式 */
</style>