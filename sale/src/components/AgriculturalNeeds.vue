<template>
  <!-- 搜索栏 -->
  <div class="container mx-auto px-4 py-4">
    <div class="flex items-center justify-center">
      <input
        v-model="searchKey"
        type="text"
        placeholder="输入关键词搜索"
        class="border border-gray-300 rounded-l px-4 py-2 w-2/3 focus:outline-none"
      />
      <button
        class="bg-[#007029] text-white px-4 py-2 rounded-r flex items-center" @click="searchNeeds()"
      >
        <search-icon class="w-4 h-4 mr-1" />
        搜索
      </button>
    </div>
  </div>
  <div class="bg-white rounded-lg">
    <!-- 产品展示区 -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-2">
      <div
        v-for="(product, index) in needsData"
        :key="index"
        class="bg-white rounded-lg shadow-sm hover:shadow-md transition-shadow duration-300"
      >
        <div class="p-2 flex">
          <!-- 产品图片 -->
          <div class="flex-shrink-0 mr-2">
            <img
              :src="$store.state.imgShowRoad + '/file/order/' + product.picture"
              class="w-40 h-36 object-cover rounded"
            />
          </div>

          <!-- 产品信息 -->
          <div class="flex-grow">
            <h3 class="text-gray-800 font-medium text-l mb-2 line-clamp-1">
              {{ product.title }}
            </h3>
            <p class="text-gray-500 text-l">{{ product.content }}</p>
            <div class="flex justify-end mt-2">
              <button 
                @click="showContactDialog(product)"
                class="text-[#007029] text-sm hover:underline"
              >
                联系信息
              </button>
            </div>
          </div>
        </div>

        <el-dialog
          v-model="contactDialogVisible"
          title="联系信息"
          width="30%"
          :modal="false"
        >
          <div class="space-y-4">
            <div>联系人：{{contPerson}}</div>
            <div>联系电话：{{contTel}}</div>
          </div>
          <template #footer>
            <span class="dialog-footer">
              <el-button @click="contactDialogVisible = false">关闭</el-button>
            </span>
          </template>
        </el-dialog>
      </div>
    </div>
  </div>
  <!-- 分页 -->
  <div class="flex justify-center items-center mt-8 text-sm">
    <Pagination
      :current-page="pagination.currentPage"
      :page-size="pagination.pageSize"
      :total="pagination.total"
      @update:current-page="setCurrentPage"
      @update:page-size="setPageSize"
      @page-change="loadData"
      class="mt-6"
    />
  </div>
</template>

<script setup>
import { ref } from "vue";
import {
  ShoppingCartIcon,
  UserIcon,
  SearchIcon,
  PlusIcon,
} from "lucide-vue-next";

import { onMounted } from "vue";
import { defineComponent } from "vue";
import { apiClient } from "../api/apiService.js";
import { useRouter } from "vue-router";
import Pagination from "../components/Pagination.vue";
import { ElMessage } from 'element-plus';

const router = useRouter();

// 联系信息弹窗
const contactDialogVisible = ref(false);
const contPerson = ref("");
const contTel = ref("");

const showContactDialog = async (item) => {
  const ownName = item.ownName;
  const res = await selectNeedsPerson({ ownName });
  if (res){
    contPerson.value = res.realName;
    contTel.value = res.phone;
    contactDialogVisible.value = true;
  }
};

// 检索关键字内容
const searchKey = ref("");
const searchFlag = ref(0);

// 分页状态
const pagination = ref({
  currentPage: 1,
  pageSize: 28,
  total: 0,
});

// 表格数据
const needsData = ref([]);

// 后端请求数据数据
const selectData = async ({ page }) => {
  try {
    //检索URL
    const url1=ref(`/order/needs/${page}`);
    const url2=ref(`/order/searchNeedsByKeys/${searchKey.value}/${page}`);
    const url = ref("");
    
    if (searchFlag.value == 1 && searchKey.value) {
      url.value = url2.value;
    } else {
      url.value = url1.value;
    }
    const response = await apiClient.get(url.value, {
      headers: {
        Authorization: window.localStorage.token,
      },
    });
    console.log("请求成功", response.data);
    return response.data;
  } catch (error) {
    console.error("请求失败", error);
    throw error;
  }
};

// 加载数据
const loadData = async ({ page }) => {
  try {
    const res = await selectData({ page });
    needsData.value = res.list;
    pagination.value.total = res.total;
  } catch (error) {
    console.error("数据加载失败:", error);
  }
};

// 更新当前页
const setCurrentPage = (page) => {
  pagination.value.currentPage = page;
};

// 更新每页条数
const setPageSize = (size) => {
  pagination.value.pageSize = size;
};

// 初始化加载
onMounted(() => {
  loadData({
    page: pagination.value.currentPage,
  });
});

//点击检索
const searchNeeds = ()=> {
  pagination.value.currentPage = 1;
  searchFlag.value = 1;
  loadData({
    page: pagination.value.currentPage,
  });
};

// 后端请求数据数据
const selectNeedsPerson = async ({ ownName }) => {
  try {
    //检索URL
    const response = await apiClient.get(`/user/${ownName}`, {
      headers: {
        Authorization: window.localStorage.token,
      },
    });

    if (response && response.flag == true) {
      return response.data;
    }
    else {
      ElMessage.error('您未登录，请先登录');
    }
  } catch (error) {
    console.error("请求失败", error);
    throw error;
  }
};
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
