<template>
  <!-- 主内容区 -->
  <div class="mx-auto px-20">
    <!-- 搜索栏 -->
    <div class="container mx-auto px-4 pb-4">
      <div class="flex items-center justify-center">
        <input
          type="text"
          v-model="searchKey"
          placeholder="输入关键词搜索"
          class="border border-gray-300 rounded-l px-4 py-2 w-2/3 focus:outline-none"
        />
        <button
          class="bg-[#007029] text-white mx-2 px-4 py-2 rounded-md flex items-center"
          @click="searchGoods()"
        >
          搜索
        </button>
      </div>
    </div>
    <!-- 商品货源 -->
    <div class="space-y-2">
      <div class="grid gap-4 md:grid-cols-3">
        <div
          v-for="(product, index) in goodsData"
          :key="index"
          class="border rounded-lg overflow-hidden"
        >
          <div class="h-40 bg-gray-100">
            <img
              :src="$store.state.imgShowRoad + '/file/order/' + product.picture"
              class="w-full h-full object-cover"
            />
          </div>
          <div class="p-4">
            <h3 class="font-medium text-lg mb-2">{{ product.title }}</h3>
            <p class="text-gray-600 text-sm mb-2 line-clamp-2">
              {{ product.content }}
            </p>
            <div class="flex justify-between items-center">
              <span class="text-red-600 font-medium">¥{{ product.price }}</span>
              <div class="flex space-x-2">
                <button
                 v-if="product.orderStatus == 1"
                  @click="submitPublish(1,product)"
                  class="text-blue-500 hover:text-blue-700"
                >
                  上架
                </button>
                <button
                v-if="product.orderStatus == 0"
                  class="text-red-500 hover:text-red-700"
                  @click="submitPublish(2,product)"
                >
                  下架
                </button>
              </div>
            </div>
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
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from "vue";
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
import { ElMessage } from "element-plus";
import { useStore } from "vuex";
import Pagination from "../components/Pagination.vue";

const store = useStore();
const router = useRouter();

// 检索关键字内容
const searchKey = ref("");
const searchFlag = ref(0);

const submitPublish = async (flag,item) => {
  try {
     //检索URL
    const url = ref("");
    if(flag ==1){
      url.value = `/order/takeUpOrder/${item.orderId}`;
    } else {
      url.value = `/order/takeDownOrder/${item.orderId}`;
    }
    //发布商品
    const responseOrder = await apiClient.put(
      url.value,
      null,
      {
        headers: {
          Authorization: window.localStorage.token,
        },
      }
    );
    if (responseOrder.flag) {
      ElMessage.success("修改成功");
    } else {
      ElMessage.error("修改失败");
    }
    searchKey.value = "";
    searchFlag.value = 0;
    pagination.value.currentPage = 1;
    loadData({
      page: pagination.value.currentPage,
    });
  } catch (error) {
    ElMessage.error("修改失败");
  }
};

// 分页状态
const pagination = ref({
  currentPage: 1,
  pageSize: 28,
  total: 0,
});

// 表格数据
const goodsData = ref([]);

// 后端请求数据数据
const selectData = async ({ page }) => {
  try {
    //检索URL
    const url1 = ref(`/order/search/goods/${page}`);
    const url2 = ref(`/order/searchMyGoodsByKeys/${searchKey.value}/${page}`);
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
    if (response.flag) {
      return response.data;
    } else {
      ElMessage.error(response.data);
    }
  } catch (error) {
    console.error("请求失败", error);
    throw error;
  }
};

// 加载数据
const loadData = async ({ page }) => {
  try {
    const res = await selectData({ page });
    goodsData.value = res.list;
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
const searchGoods = () => {
  pagination.value.currentPage = 1;
  searchFlag.value = 1;
  loadData({
    page: pagination.value.currentPage,
  });
};
</script>

<style scoped>
/* 可以添加额外的样式 */
</style>