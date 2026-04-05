import axios from 'axios'
import type { Log } from '../types/log'

const BASE_URL = 'http://localhost:8080'

export const fetchLogs = async (projectId: string, page = 0, size = 20): Promise<{
  content: Log[]
  totalElements: number
  totalPages: number
  number: number
}> => {
  const { data } = await axios.get(`${BASE_URL}/api/logs/${projectId}`, {
    params: { page, size }
  })
  return data
}
