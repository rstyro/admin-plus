<script setup lang="ts">
/**
 * @file 富文本编辑器 Demo
 * @description 由旧版 templates/page/tinymce/tinymceDemo.html 重构而来，
 *   使用自托管 TinyMCE（web/public/tinymce），初始化配置与旧版保持一致。
 */
import { onBeforeUnmount, onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'

defineOptions({ name: 'TinymceDemo' })

const title = ref('')
const content = ref('')

/** TinyMCE 编辑器实例 */
let editor: any = null

let scriptPromise: Promise<any> | null = null
/** 按需加载自托管的 tinymce.min.js（全局只加载一次） */
function loadTinymce(): Promise<any> {
  const w = window as any
  if (w.tinymce) return Promise.resolve(w.tinymce)
  if (!scriptPromise) {
    scriptPromise = new Promise((resolve, reject) => {
      const script = document.createElement('script')
      script.src = '/tinymce/tinymce.min.js'
      script.async = true
      script.onload = () => resolve((window as any).tinymce)
      script.onerror = () => {
        scriptPromise = null
        reject(new Error('TinyMCE 脚本加载失败'))
      }
      document.head.appendChild(script)
    })
  }
  return scriptPromise
}

/** 上传文件并回调 location（对应旧版 file_picker_callback） */
function uploadFile(file: File, callback: (url: string) => void) {
  const xhr = new XMLHttpRequest()
  xhr.withCredentials = false
  xhr.open('POST', '/upload/tinyFile')
  xhr.onload = () => {
    if (xhr.status !== 200) {
      ElMessage.error(`HTTP Error: ${xhr.status}`)
      return
    }
    let json: any
    try {
      json = JSON.parse(xhr.responseText)
    } catch {
      ElMessage.error(`Invalid JSON: ${xhr.responseText}`)
      return
    }
    if (!json || typeof json.location != 'string') {
      ElMessage.error(`Invalid JSON: ${xhr.responseText}`)
      return
    }
    callback(json.location)
  }
  xhr.onerror = () => ElMessage.error('文件上传失败')
  const formData = new FormData()
  formData.append('file', file, file.name)
  xhr.send(formData)
}

/** 与旧版 tinymceDemo.html 一致的初始化配置 */
const tinymceSettings = {
  license_key: 'gpl',
  language: 'zh_CN',
  plugins:
    'image link autolink code table preview codesample emoticons fullscreen lists advlist media pagebreak searchreplace',
  toolbar:
    'code undo redo restoredraft | cut copy paste pastetext | forecolor backcolor bold italic underline strikethrough link anchor | alignleft aligncenter alignright alignjustify outdent indent | ' +
    'styleselect formatselect fontselect fontsizeselect | bullist numlist | blockquote subscript superscript removeformat | ' +
    'table image media charmap emoticons pagebreak insertdatetime preview | fullscreen | lineheight',
  images_upload_url: '/upload/tinyImage',
  images_upload_base_path: '',
  max_height: 750,
  fontsize_formats: '12px 14px 16px 18px 24px 36px 48px 56px 72px',
  font_formats:
    '微软雅黑=Microsoft YaHei,Helvetica Neue,PingFang SC,sans-serif;苹果苹方=PingFang SC,Microsoft YaHei,sans-serif;宋体=simsun,serif;仿宋体=FangSong,serif;黑体=SimHei,sans-serif;Arial=arial,helvetica,sans-serif;Arial Black=arial black,avant garde;Book Antiqua=book antiqua,palatino;',
  toolbar_sticky: true,
  autosave_ask_before_unload: false,
  file_picker_callback(callback: (url: string) => void, _value: string, meta: { filetype?: string }) {
    let filetype = '.pdf, .txt, .zip, .rar, .7z, .doc, .docx, .xls, .xlsx, .ppt, .pptx, .mp3, .mp4'
    switch (meta.filetype) {
      case 'image':
        filetype = '.jpg, .jpeg, .png, .gif'
        break
      case 'media':
        filetype = '.mp3, .mp4'
        break
    }
    const input = document.createElement('input')
    input.setAttribute('type', 'file')
    input.setAttribute('accept', filetype)
    input.click()
    input.onchange = () => {
      const file = input.files?.[0]
      if (file) uploadFile(file, callback)
    }
  },
}

onMounted(async () => {
  try {
    const tinymce = await loadTinymce()
    await tinymce.init({
      ...tinymceSettings,
      selector: '#tinymce-demo-input',
      setup(inst: any) {
        editor = inst
        inst.on('change', () => {
          content.value = inst.getContent()
        })
      },
    })
  } catch (e: any) {
    ElMessage.error(e?.message || '富文本编辑器初始化失败')
  }
})

onBeforeUnmount(() => {
  if (editor) {
    content.value = editor.getContent()
  }
  ;(window as any).tinymce?.remove('#tinymce-demo-input')
  editor = null
})

function onSubmit() {
  const html = editor ? editor.getContent() : content.value
  // eslint-disable-next-line no-console
  console.log('formDto=', { title: title.value, content: html })
  ElMessage.success('内容已输出到控制台')
}
</script>

<template>
  <el-card shadow="never">
    <template #header>
      <span>富文本编辑器 Demo</span>
    </template>

    <el-form label-width="80px">
      <el-form-item label="标题">
        <el-input v-model="title" />
      </el-form-item>
      <el-form-item label="内容">
        <textarea id="tinymce-demo-input"></textarea>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">立即创建</el-button>
        <el-button @click="title = ''">取消</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<style>
/* 编辑器占满表单内容区 */
.tox-tinymce {
  width: 100%;
}
/* 让 tinymce 浮动菜单/弹窗在最上层（旧版同款处理） */
.tox-tinymce-aux {
  z-index: 9999 !important;
}
</style>
