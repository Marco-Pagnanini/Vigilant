import axios from 'axios'
import type { Log } from '../types/log'
import { getToken } from './auth'

const BASE_URL = import.meta.env.VITE_API_BASE_URL ?? ''

export const fetchLogs = async (projectId: string, page = 0, size = 20): Promise<{
  content: Log[]
  totalElements: number
  totalPages: number
  number: number
}> => {
  const { data } = await axios.get(`${BASE_URL}/api/logs/${projectId}`, {
    params: { page, size },
    headers: { Authorization: `Bearer ${getToken()}` }
  })
  return data
}
