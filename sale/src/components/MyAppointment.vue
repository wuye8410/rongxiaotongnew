<template>
  <!-- 主内容区 -->
  <div class="mx-auto px-20">
    <!-- 农业问答 -->
    <div class="space-y-2">
      <div class="max-w-3xl mx-auto space-y-4">
        <div
          v-for="(product, index) in appoints"
          :key="index"
          class="bg-white rounded-lg shadow p-6 relative"
        >
          <!-- Status tag -->
          <div class="absolute top-4 right-4">
            <span
              
              class="px-3 py-1 text-sm rounded-md text-white"
              :style="{ backgroundColor: product.status == 1 ? '#059669' : '#eab308' }"
            >
              {{ product.status == 1 ? "已答复" : "未答复" }}
            </span>
          </div>

          <!-- Product title -->
          <h3 class="text-lg font-medium text-gray-900">{{ product.name }}</h3>

          <!-- Product variety -->
          <p class="text-sm text-gray-700">
            <span class="text-gray-500 mr-2">农作物:</span>
            <span>{{ product.plantName }}</span>
          </p>

          <!-- Soil conditions -->

          <p class="text-sm text-gray-700">
            <span class="text-gray-500 mr-2">作物详细信息：</span>
            <span>{{ product.plantDetail }}</span>
          </p>

          <p class="text-sm text-gray-700">
            <span class="text-gray-500 mr-2">生长情况:</span>
            <span>{{ product.plantCondition }}</span>
          </p>
          <p class="text-sm text-gray-700">
            <span class="text-gray-500 mr-2">土壤条件:</span>
            <span>{{ product.soilCondition }}</span>
          </p>
          <p class="text-sm text-gray-700">
            <span class="text-gray-500 mr-2">种植面积（亩）:</span>
            <span>{{ product.area }}</span>
          </p>
          <p class="text-sm text-gray-700">
            <span class="text-gray-500 mr-2">联系方式:</span>
            <span>{{ product.phone }}</span>
          </p>
          <p class="text-sm text-gray-700">
            <span class="text-gray-500 mr-2">地址:</span>
            <span>{{ product.address }}</span>
          </p>

          <!-- Divider -->
          <div class="border-t border-gray-200 my-4"></div>

          <!-- Action buttons -->
          <div class="flex justify-end space-x-3">
            <button
              @click="editProduct(product)" v-if="$store.getters.isExpert"
              class="px-6 py-1.5 border border-green-600 text-green-600 rounded-md text-sm hover:bg-green-50"
            >
              编辑
            </button>

            <button
              v-if="!$store.getters.isExpert"
              @click="confirmDelete(product)"
              class="px-6 py-1.5 border border-gray-300 text-gray-600 rounded-md text-sm hover:bg-gray-50"
            >
              删除
            </button>
          </div>
        </div>
        <div v-if="appoints.length === 0" class="px-3 py-10 text-center text-gray-500">        
      暂无数据        
      </div>
      </div>
    </div>
    <!-- Edit Modal -->
    <div
      v-if="showEditModal"
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
    >
      <div class="bg-white rounded-lg w-full max-w-lg mx-4 p-6">
        <h3 class="text-lg font-medium mb-4">编辑产品</h3>

        <div class="space-y-4" v-if="!$store.getters.isExpert">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1"
              >农作物：</label
            >
            <input
              v-model="editingProduct.plantName"
              class="w-full border border-gray-300 rounded-md p-2 text-sm"
            />
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1"
              >作物详细信息：</label
            >
            <input
              v-model="editingProduct.plantDetail"
              class="w-full border border-gray-300 rounded-md p-2 text-sm"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1"
              >占地面积：</label
            >
            <input
              v-model="editingProduct.area"
              class="w-full border border-gray-300 rounded-md p-2 text-sm"
            />
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1"
              >生长情况：</label
            >
            <textarea
              v-model="editingProduct.plantCondition"
              rows="2"
              class="w-full border border-gray-300 rounded-md p-2 text-sm"
            ></textarea>
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1"
              >土壤条件：</label
            >
            <input
              v-model="editingProduct.soilCondition"
              class="w-full border border-gray-300 rounded-md p-2 text-sm"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1"
              >联系方式：</label
            >
            <input
              v-model="editingProduct.phone"
              class="w-full border border-gray-300 rounded-md p-2 text-sm"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1"
              >地址：</label
            >
            <input
              v-model="editingProduct.address"
              class="w-full border border-gray-300 rounded-md p-2 text-sm"
            />
          </div>
        </div>
        <div class="space-y-4" v-if="$store.getters.isExpert">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1"
              >答复：</label
            >
            <input
              v-model="editingProduct.answer"
              class="w-full border border-gray-300 rounded-md p-2 text-sm"
            />
          </div>
        </div>

        <div class="flex justify-end space-x-3 mt-6">
          <button
            @click="cancelEdit"
            class="px-4 py-2 border border-gray-300 text-gray-700 rounded-md text-sm hover:bg-gray-50"
          >
            取消
          </button>
          <button
            @click="saveEdit"
            class="px-4 py-2 bg-green-600 text-white rounded-md text-sm hover:bg-green-700"
          >
            保存
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from "vue";
import { onMounted } from "vue";
import { defineComponent } from "vue";
import { apiClient } from "../api/apiService.js";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import { useStore } from "vuex";

const store = useStore();
const router = useRouter();

const appoints = ref([]);

// 后端请求数据数据
const selectData = async ({}) => {
  try {
    //检索URL
    const role = store.getters.isExpert ? "expert" : "questioner";
    const url = ref(`/reserve/selectByKind/${role}`);

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
const loadData = async ({}) => {
  try {
    const res = await selectData({});
    appoints.value = res;
  } catch (error) {
    console.error("数据加载失败:", error);
  }
};

// 初始化加载
onMounted(() => {
  loadData({});
});

// Edit functionality
const showEditModal = ref(false);

const editingProduct = reactive({
  id: "",
  expertName: "",
  questioner: "",
  area: "",
  address: "",
  plantName: "",
  soilCondition: "",
  plantCondition: "",
  plantDetail: "",
  phone: "",
  message: "",
  answer: "",
  status: "0",
});

const editProduct = (item) => {
  editingProduct.id = item.id;
  editingProduct.expertName = item.expertName;
  editingProduct.questioner = item.questioner;
  editingProduct.area = item.area;
  editingProduct.address = item.address;
  editingProduct.plantName = item.plantName;
  editingProduct.soilCondition = item.soilCondition;
  editingProduct.plantCondition = item.plantCondition;
  editingProduct.plantDetail = item.plantDetail;
  editingProduct.phone = item.phone;
  editingProduct.message = item.message;
  editingProduct.answer = item.answer;
  editingProduct.status = item.status;
  showEditModal.value = true;
};

const cancelEdit = () => {
  showEditModal.value = false;
};

const saveEdit = async () => {
  try {
    //检查答案
    if (store.getters.isExpert) {
      if (!editingProduct.answer) {
        ElMessage.error("答复内容不能为空");
        return;
      }
    } else {
      if (!editingProduct.area || !editingProduct.address ||!editingProduct.plantName ||!editingProduct.soilCondition ||!editingProduct.plantCondition ||
        !editingProduct.plantDetail ||!editingProduct.phone) {
        ElMessage.error("请填写完整内容");
        return;
      }
    }

    //修改
    const param = ref({
      id: editingProduct.id,
      expertName: editingProduct.expertName,
      questioner: editingProduct.questioner,
      area: editingProduct.area,
      address: editingProduct.address,
      plantName: editingProduct.plantName,
      soilCondition: editingProduct.soilCondition,
      plantCondition: editingProduct.plantCondition,
      plantDetail: editingProduct.plantDetail,
      phone: editingProduct.phone,
      message: editingProduct.message,
      answer: editingProduct.answer,
      status: editingProduct.status,
    });
    if (store.getters.isExpert) {
      param.value.status = 1;
    }
    //发布商品
    const responseOrder = await apiClient.put(`/reserve/update`, param.value, {
      headers: {
        Authorization: window.localStorage.token,
      },
    });

    if (responseOrder.flag) {
      ElMessage.success("修改成功");
    } else {
      ElMessage.error("修改失败");
    }
    //关闭编辑框
    showEditModal.value = false;
    //重新加载数据
    loadData({});
  } catch (error) {
    console.error("修改失败:", error);
    ElMessage.error("修改失败");
  }
};

const confirmDelete = async (item) => {
  //检查是否已经回答
  if (item.status == 1) {
    ElMessage.error("已经答复的预约不能删除");
    return;
  }
  try {
    if (confirm("确定要删除预约吗？")) {
      //删除
      const response = await apiClient.delete(`/reserve/delete/${item.id}`, {
        headers: {
          Authorization: window.localStorage.token,
        },
      });
      console.log("Delete response:", response); // Debug logging
      if (response.flag == true) {
        ElMessage.success("删除预约成功。");
      } else {
        ElMessage.error("删除预约失败，请重试。");
      }

      //重新加载数据
    loadData({});
    }
  } catch (error) {
    console.error("删除预约失败，请重试:", error);
  }
};
</script>

<style>
/* Tailwind's line-clamp utility for truncating text */
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
