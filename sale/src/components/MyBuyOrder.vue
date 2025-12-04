<template>
  <!-- 主内容区 -->
  <div class="mx-auto px-20">
    <!-- 我买到的 -->
    <div class="space-y-2">
      <div class="grid gap-4 md:grid-cols-3">
        <div
          v-for="(product, index) in products"
          :key="index"
          class="border rounded-lg overflow-hidden"
        >
          <div class="h-40 bg-gray-100">
            <img
              :src="$store.state.imgShowRoad + '/file/order/' + product.picture"
              alt="商品图片"
              class="w-full h-full object-cover"
            />
          </div>
          <div class="p-4">
            <p class="text-gray-600 text-sm line-clamp-2">
              收货人：{{ product.ownName }}
            </p>
            <p class="text-gray-600 text-sm line-clamp-2">
              下单时间：{{product.updateTime}}
            </p>
            <p class="text-gray-600 text-sm line-clamp-2">
              收货地址：{{ product.address }}
            </p>
            <div class="flex justify-between items-center mt-2">
              <span class="text-red-600 font-medium">¥{{ product.totalPrice }}</span>
              <span 
            class="px-3 py-1 text-sm rounded-full"
            :class="product.purchaseStatus===1 ? 'bg-yellow-100 text-yellow-800' : 'bg-green-100 text-green-800'"
          >
            {{ product.purchaseStatus ===1? '已发货' : '未发货' }}
          </span>
            </div>
          </div>
        </div>
      </div>
      <div v-if="products.length === 0" class="px-3 py-10 text-center text-gray-500">        
      暂无数据        
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

// 商品货源数据
const products = ref([]);

// 初始化加载
onMounted(async() => {
  const res = await selectData({});
  
  res.forEach((item) => {
    item['updateTime'] = item['updateTime'].split('T')[0];
  });

  products.value = res;
});

// 后端请求数据数据
const selectData = async ({  }) => {
  try {
    //检索URL
    const url = ref(`/order/selectBuys`);

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

<style scoped>
/* 可以添加额外的样式 */
</style>