<template>
  <!-- 主内容区 -->
  <div class="mx-auto px-20">
    <!-- 农业问答 -->
    <div class="space-y-2">
      <div class="max-w-3xl mx-auto space-y-4">
        <div
          v-for="(qa, index) in qaItems"
          :key="index"
          class="bg-white rounded-lg shadow p-6 relative"
        >
          <!-- Answered badge -->
          <div class="absolute top-4 right-4">
            <button
              class="px-3 py-1 text-sm rounded-md text-white"
              :style="{ backgroundColor: qa.answer ? '#059669' : '#eab308' }"
            >
              {{ qa.answer ? "已回答" : "未回答" }}
            </button>
          </div>

          <!-- Question -->
          <div class="flex items-start mb-4">
            <div
              class="w-6 h-6 rounded-full bg-green-600 flex items-center justify-center text-white mr-2 flex-shrink-0"
            >
              <span class="text-xs">Q</span>
            </div>
            <h3 class="text-lg font-medium">{{ qa.question }}</h3>
          </div>

          <!-- Answer -->
          <div class="flex">
            <div
              class="w-6 h-6 rounded-full bg-orange-200 flex items-center justify-center text-gray-600 mr-2 flex-shrink-0"
            >
              <span class="text-xs">A</span>
            </div>
            <div>
              <p class="text-sm text-gray-700">
                {{ qa.answer }}
              </p>
            </div>
          </div>

          <!-- Metadata -->
          <div class="flex flex-wrap items-center text-xs text-gray-500 mt-4">
            <div class="mr-4">作者: {{ qa.expertName }}</div>

            <div class="flex items-center mr-4">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                class="h-4 w-4 mr-1"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"
                />
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"
                />
              </svg>
              <span>606</span>
            </div>

            <div class="flex items-center mr-4">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                class="h-4 w-4 mr-1"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M11.049 2.927c.3-.921 1.603-.921 1.902 0l1.519 4.674a1 1 0 00.95.69h4.915c.969 0 1.371 1.24.588 1.81l-3.976 2.888a1 1 0 00-.363 1.118l1.518 4.674c.3.922-.755 1.688-1.538 1.118l-3.976-2.888a1 1 0 00-1.176 0l-3.976 2.888c-.783.57-1.838-.197-1.538-1.118l1.518-4.674a1 1 0 00-.363-1.118l-3.976-2.888c-.784-.57-.38-1.81.588-1.81h4.914a1 1 0 00.951-.69l1.519-4.674z"
                />
              </svg>
              <span>收藏(21)</span>
            </div>

            <div class="flex items-center">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                class="h-4 w-4 mr-1"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M14 10h4.764a2 2 0 011.789 2.894l-3.5 7A2 2 0 0115.263 21h-4.017c-.163 0-.326-.02-.485-.06L7 20m7-10V5a2 2 0 00-2-2h-.095c-.5 0-.905.405-.905.905 0 .714-.211 1.412-.608 2.006L7 11v9m7-10h-2M7 20H5a2 2 0 01-2-2v-6a2 2 0 012-2h2.5"
                />
              </svg>
              <span>喜欢(12)</span>
            </div>

            
            <div class="ml-auto">
              
              <div class="flex justify-end space-x-3">
                <button
                  @click="openEditModal(qa)"
                  v-if="$store.getters.isExpert"
                  class="px-6 py-1.5 border border-green-600 text-green-600 rounded-md text-sm hover:bg-green-50"
                >
                  编辑
                </button>

                <button
                  v-if="!$store.getters.isExpert"
                  @click="delQuestion(qa)"
                  class="px-6 py-1.5 border border-gray-300 text-gray-600 rounded-md text-sm hover:bg-gray-50"
                >
                  删除
                </button>
              </div>
            </div>
          </div>
          
        </div>
        <div v-if="qaItems.length === 0" class="px-3 py-10 text-center text-gray-500">        
      暂无数据        
      </div>
      </div>
    </div>
    <div
      v-if="showEditModal"
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
    >
      <div class="bg-white rounded-lg w-full max-w-lg mx-4">
        <!-- Modal Header with green underline -->
        <div class="relative p-4">
          <h3 class="text-lg font-medium">编辑问答</h3>
          <button
            @click="closeEditModal"
            class="absolute top-4 right-4 text-gray-500 hover:text-gray-700"
          >
            <span class="text-xl">X</span>
          </button>
          <!-- Green underline -->
          <div class="absolute bottom-0 left-0 w-16 h-1 bg-green-600"></div>
        </div>

        <div class="border-t border-gray-100"></div>

        <!-- Modal Body -->
        <div class="p-4">
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-1"
              >问题标题:</label
            >
            <div class="text-sm text-gray-900">
              {{ editingQue }}
            </div>
          </div>

          <div class="mb-4">
            <label
              for="answer"
              class="block text-sm font-medium text-gray-700 mb-1"
              >回答内容:</label
            >
            <textarea
              id="answer"
              v-model="editingAnswer"
              rows="6"
              class="w-full border border-gray-300 rounded-md p-2 text-sm"
              placeholder="请输入回答内容..."
            ></textarea>
          </div>
        </div>

        <!-- Modal Footer -->
        <div class="p-4 flex justify-center">
          <button
            @click="saveEdit"
            class="px-8 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 focus:outline-none"
          >
            保存
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from "vue";
import { onMounted } from "vue";
import { defineComponent } from "vue";
import { apiClient } from "../api/apiService.js";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import { useStore } from "vuex";

const store = useStore();
const router = useRouter();

const qaItems = ref([]);

// 后端请求数据数据
const selectData = async ({}) => {
  try {
    //检索URL
    const role = store.getters.isExpert ? "expert" : "questioner";
    const url = ref(`/question/selectByKind/${role}`);

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
    qaItems.value = res;
  } catch (error) {
    console.error("数据加载失败:", error);
  }
};

// 初始化加载
onMounted(() => {
  loadData({});
});

//点击检索
const searchGoods = () => {
  searchFlag.value = 1;
  loadData({});
};

// Edit modal functionality
const showEditModal = ref(false);
const editingQue = ref("");
const editingAnswer = ref("");

// 分页状态
const currentQue = ref();

const openEditModal = (item) => {
  currentQue.value = item;
  editingQue.value = item.question;
  editingAnswer.value = item.answer;
  showEditModal.value = true;
};

const closeEditModal = () => {
  showEditModal.value = false;
  editingQue.value = "";
  editingAnswer.value = "";
};
const delQuestion = async (item) => {
  //检查是否已经回答
  if (item.status == 1) {
    ElMessage.error("已经回答的问题不能删除");
    return;
  }
  try {
    if (confirm("确定要删除问题吗？")) {
      //删除
      const response = await apiClient.delete(`/question/delete/${item.id}`, {
        headers: {
          Authorization: window.localStorage.token,
        },
      });
      console.log("Delete response:", response); // Debug logging
      if (response.flag == true) {
        ElMessage.success("删除问题成功。");
      } else {
        ElMessage.error("删除问题失败，请重试。");
      }

      //重新查询问答
      loadData({});
    }
  } catch (error) {
    console.error("删除问题失败，请重试:", error);
  }
};

const saveEdit = async () => {
  //检查答案
  if (editingAnswer.value.trim().length == 0) {
    ElMessage.error("答案内容不能为空");
    return;
  }

  try {
    //修改
    const param = ref({
      id: currentQue.value.id,
      title: currentQue.value.title,
      plantName: currentQue.value.crop,
      phone: currentQue.value.contact,
      questioner: currentQue.value.questioner,
      expertName: currentQue.value.expertName,
      status: 1,
      question: currentQue.value.question,
      answer: editingAnswer.value,
    });
    //发布商品
    const responseOrder = await apiClient.put(`/question/update`, param.value, {
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
    closeEditModal();
    //重新查询问答
    loadData({});
  } catch (error) {
    console.error("修改失败:", error);
    ElMessage.error("修改失败");
  }
};
</script>

<style>
/* Tailwind's line-clamp utility for truncating text */
.line-clamp-4 {
  display: -webkit-box;
  -webkit-line-clamp: 4;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>