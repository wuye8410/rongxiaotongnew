<template>
  <!-- 主内容区 -->
  <div class="mx-auto px-20">

    <!-- 商品货源 -->
    <div class="space-y-2">
    <div class="max-w-3xl mx-auto space-y-4">
      <div 
        v-for="order in orders" 
        :key="order.id" 
        class="bg-white rounded-lg shadow p-4 relative"
      >
        <div class="absolute top-4 right-4">
          <span 
            class="px-3 py-1 text-sm rounded-full"
            :class="order.purchaseStatus ? 'bg-yellow-100 text-yellow-800' : 'bg-green-100 text-green-800'"
          >
            {{ order.purchaseStatus ? '已发货' : '未发货' }}
          </span>
        </div>
        <div class="grid grid-cols-2 gap-2 text-sm">
          <div class="flex">
            <span class="text-gray-500 mr-2">姓名:</span>
            <span>{{ order.ownName }}</span>
          </div>
          <div class="flex">
            <span class="text-gray-500 mr-2">手机号:</span>
            <span>{{ order.phone }}</span>
          </div>
          <div class="flex">
            <span class="text-gray-500 mr-2">下单时间:</span>
            <span>{{ order.updateTime }}</span>
          </div>
        </div>
        <div class="mt-2 text-sm">
          <span class="text-gray-500 mr-2">收货地址:</span>
          <span>{{ order.address }}</span>
        </div>
        <div class="mt-2">
          <span class="text-red-500 font-medium">¥{{ order.sumPrice }}</span>
        </div>
      </div>
      <div v-if="orders.length === 0" class="px-3 py-10 text-center text-gray-500">        
      暂无数据        
      </div>
    </div>

    </div>

  </div>
</template>

<script setup>
import { ref, reactive } from "vue";
import { onMounted } from "vue";
import { defineComponent } from "vue";
import { apiClient } from "../api/apiService.js";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import { useStore } from "vuex";

const store = useStore();
const router = useRouter();

// Sample data - in a real app, this would come from props or an API
const orders = ref([]);

// 初始化加载
onMounted(async() => {
  const res = await selectData({});
  
  res.forEach((item) => {
    item['updateTime'] = item['updateTime'].split('T')[0];
  });

  orders.value = res;
});

// 后端请求数据数据
const selectData = async ({  }) => {
  try {
    //检索URL
    const url = ref(`/order/selectSells`);

    const response = await apiClient.get(url.value, {
      headers: {
        Authorization: window.localStorage.token
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

</script>