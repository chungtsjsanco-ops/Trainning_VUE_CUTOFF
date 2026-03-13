<template>
  <b-form @submit.prevent="submit">
    <b-form-group label="Họ và tên" label-for="ucf-name" class="mb-3">
      <b-form-input
        id="ucf-name"
        v-model.trim="form.name"
        placeholder="Nhập họ và tên"
        :state="nameState"
        autocomplete="off"
      />
      <b-form-invalid-feedback :state="nameState">Vui lòng nhập họ và tên.</b-form-invalid-feedback>
    </b-form-group>

    <b-form-group label="Email" label-for="ucf-email" class="mb-3">
      <b-form-input
        id="ucf-email"
        v-model.trim="form.email"
        type="email"
        placeholder="name@example.com"
        :state="emailState"
        autocomplete="off"
      />
      <b-form-invalid-feedback :state="emailState">Email không hợp lệ.</b-form-invalid-feedback>
    </b-form-group>

    <b-form-group label="Vai trò" label-for="ucf-role" class="mb-3">
      <b-form-select id="ucf-role" v-model="form.role" :options="roleOptions" />
    </b-form-group>

    <div class="d-flex justify-content-end">
      <b-button variant="outline-secondary" class="mr-2" @click="$emit('cancel')">Hủy</b-button>
      <b-button variant="primary" type="submit" :disabled="busy || !canSubmit">
        <b-spinner small v-if="busy" class="mr-1" />
        Lưu
      </b-button>
    </div>
  </b-form>
</template>

<script>
export default {
  name: 'UserCreateForm',
  props: {
    busy: { type: Boolean, default: false }
  },
  data: () => ({
    form: {
      name: '',
      email: '',
      role: 'User'
    },
    roleOptions: [
      { value: 'Admin', text: 'Admin' },
      { value: 'User', text: 'User' },
      { value: 'Editor', text: 'Editor' },
      { value: 'Viewer', text: 'Viewer' }
    ]
  }),
  computed: {
    nameState() {
      return this.form.name.length ? true : null
    },
    emailState() {
      if (!this.form.email.length) return null
      return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.form.email)
    },
    canSubmit() {
      return this.form.name.length > 0 && this.emailState === true
    }
  },
  methods: {
    reset() {
      this.form = { name: '', email: '', role: 'User' }
    },
    submit() {
      if (!this.canSubmit) return
      this.$emit('submit', { ...this.form })
    }
  }
}
</script>

