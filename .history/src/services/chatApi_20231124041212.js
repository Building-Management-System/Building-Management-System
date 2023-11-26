import { toast } from 'react-toastify'
import axiosClient from '../utils/axios-config'
import { BASE_URL } from './constraint'

const chatApi = {
  sendMessage: async (data) => {
    try {
      const response = await axiosClient.post(`${BASE_URL}/chat`, data)
      return response
    } catch (error) {
      if (error.response.status === 400) {
        toast.error('User not found!')
      }
    }
  },
  createNewMessage: async (data) => {
    try {
      await axiosClient.post(`${BASE_URL}/createNewChat`, data)
      toast.success('Create new message successfully!!!!')
    } catch (error) {
      if (error.response.status === 400) {
        toast.error('User not found!')
      }
    }
  },
  getUserSingleChat:  (data) => {
    try {
      const res = axiosClient.post(`${BASE_URL}/getAllChatUserSingle`, data)
      return res
    } catch (error) {
      if (error.response.status === 400) {
        toast.error('User not found!')
      }
    }
  },
  getAllChatList:  (data) => {
    try {
      const res = axiosClient.post(`${BASE_URL}/getAllChat`, data)
      return res
    } catch (error) {
      if (error.response.status === 400) {
        toast.error('User not found!')
      }
    }
  }
}

export default chatApi
