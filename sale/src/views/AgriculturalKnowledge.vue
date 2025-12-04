<template>
  <section class="py-12 bg-white border-t-2 border-gray-300">
    <div class="container w-2/3 mx-auto">
      <!-- Back Button -->
      <div class="mb-6">
        <button class="flex items-center text-gray-500 hover:text-gray-700" @click="goBack">
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
              d="M15 19l-7-7 7-7"
            />
          </svg>
          返回
        </button>
      </div>
      <!-- 文章头部 -->
      <div class="p-2">
        <h1 class="text-xl font-bold mb-3">
          {{title}}
        </h1>
        <div class="flex items-center text-sm text-gray-500 mb-6">
          <span>发布时间: {{updateTime}}</span>
        </div>

        <!-- 文章内容 -->
        <div class="text-gray-700 text-sm leading-relaxed mb-6">
          <!-- 文章图片 -->
          <div class="my-6 flex justify-center">
            <img
              :src="$store.state.imgShowRoad + '/file/order/' + picPath"
              class="w-2/5 h-auto rounded-lg"
            />
          </div>

          <p class="mb-4">
            {{content}}
          </p>
        </div>
      </div>

      <!-- 评论区 -->
      <div class="border-t border-gray-100 p-6">
        <div class="flex items-center mb-6">
          <h2 class="font-medium">评论</h2>
          <span
            class="ml-2 bg-gray-100 text-gray-600 text-xs px-2 py-1 rounded-full"
            >{{ commentsNum }}</span
          >
        </div>

        <!-- 评论输入框 -->
        <div class="flex mb-8">
          <div class="mr-3 flex-shrink-0">
            <div
              class="w-10 h-10 rounded-full bg-[#007029] flex items-center justify-center overflow-hidden"
            >
              <user-icon class="w-6 h-6 text-white" />
            </div>
          </div>
          <div class="flex-1 relative">
            <textarea
              v-model="newComment"
              class="w-full border border-gray-200 rounded-lg p-3 text-sm focus:outline-none focus:border-[#007029] resize-none"
              placeholder="写下你的评论..."
              rows="2"
            ></textarea>
            <div class="flex justify-end mt-2">
              <button
                @click="addComment"
                class="bg-[#007029] text-white px-4 py-1 rounded-md text-sm hover:bg-green-700 transition-colors"
                :disabled="!newComment"
              >
                发表
              </button>
            </div>
          </div>
        </div>

        <!-- 评论列表 -->
        <div class="space-y-6">
          <div v-for="(comment, index) in comments" :key="index" class="flex">
            <div class="mr-3 flex-shrink-0">
              <div class="w-10 h-10 rounded-full overflow-hidden">
                <img
                  v-if="comment.avatar"
                  :src="comment.avatar"
                  :alt="comment.username"
                  class="w-full h-full object-cover"
                />
                <div
                  v-else
                  class="w-full h-full bg-[#007029] flex items-center justify-center"
                >
                  <user-icon class="w-6 h-6 text-white" />
                </div>
              </div>
            </div>
            <div class="flex-1">
              <div class="flex items-center mb-1">
                <h3 class="font-medium text-sm">{{ comment.username }}</h3>
                <span
                  v-if="comment.isAuthor"
                  class="ml-2 text-xs text-green-600 border border-green-600 rounded px-1"
                  >作者</span
                >
              </div>
              <p class="text-sm text-gray-700 mb-2">{{ comment.content }}</p>
              <div class="flex items-center text-xs text-gray-500">
                <span>{{ comment.date }}</span>
                <span class="mx-2">·</span>
                <span>{{ comment.location }}</span>
                <div class="flex-1"></div>
                <button
                  class="flex items-center hover:text-gray-700"
                  :class="{ 'text-green-600': comment.liked }"
                  @click="toggleLike(index)"
                >
                  <ThumbsUp
                    class="w-4 h-4 mr-1"
                    :class="{ 'fill-[#3CB371]': comment.liked }"
                  />
                  <span>{{ comment.likes > 0 ? comment.likes : "赞" }}</span>
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import {
  UserIcon,
  SmileIcon,
  MessageSquareIcon,
  ThumbsUp,
} from "lucide-vue-next";
import { useRoute, useRouter } from "vue-router";
import { ref, onMounted } from "vue";
import { apiClient } from "../api/apiService.js";
import { ElMessage } from 'element-plus';

const route = useRoute();
const router = useRouter();

let knowledgeId = ref();
let picPath = ref();
let title = ref();
let content = ref();
let ownName = ref();
let updateTime = ref();
let comments = ref([]);
let commentsNum = ref();

onMounted(async () => {
  knowledgeId.value = route.query.knowledgeId;
  picPath.value = route.query.picPath;
  title.value = route.query.title;
  content.value = route.query.content;
  ownName.value = route.query.ownName;
  updateTime.value = route.query.updateTime.split('T')[0];

  //获取评论
  const commentsData = await selectDiscuss(knowledgeId);
  comments.value = commentsData;
  commentsNum = commentsData.length;

});

const goBack = () => {
  // 返回时携带当前页面状态（需配合路由的state配置）
  router.back();
};

// 专家详情查询接口
const selectDiscuss = async (knowledgeId) => {
  try {
    const response = await apiClient.get(`/knowledge/selectByKnowledge/${knowledgeId.value}`, {
      headers: {
        Authorization: window.localStorage.token,
      },
    });
    return response.data;
  } catch (error) {
    throw error;
  }
};

// 新评论内容
const newComment = ref("");

// 添加评论
const addComment = async () => {
  if (!newComment.value.trim()) return;

  try {
    
    const response = await apiClient.post(`/knowledge/addByKnowledge/${knowledgeId.value}/${newComment.value}`,null, {
    headers: {
            Authorization: window.localStorage.token,
          },
  });
    if (response.flag == true) {
      const commentsData = await selectDiscuss(knowledgeId);
      comments.value = commentsData;
      commentsNum = commentsData.length;

      newComment.value = "";

    }
    else {
      ElMessage.error('您未登录，请先登录');
    }
  } catch (error) {
    ElMessage.error('您未登录，请先登录');
  }

};

// 点赞/取消点赞
const toggleLike = (index) => {
  const comment = comments.value[index];
  if (comment.liked) {
    comment.likes = Math.max(0, comment.likes - 1);
  } else {
    comment.likes += 1;
  }
  comment.liked = !comment.liked;
};
</script>
