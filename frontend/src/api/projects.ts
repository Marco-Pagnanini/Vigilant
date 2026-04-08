import axios from 'axios'
import type { Project } from '../types/project'
import { getToken } from './auth'

const BASE_URL = 'http://localhost:8080'

export const fetchProjects = async (): Promise<Project[]> => {
  const { data } = await axios.get(`${BASE_URL}/api/projects`, {
    headers: { Authorization: `Bearer ${getToken()}` }
  })
  return data
}

export const createProject = async (name: string, description: string): Promise<Project & { apiKey: string }> => {
  const { data } = await axios.post(`${BASE_URL}/api/projects`, { name, description }, {
    headers: { Authorization: `Bearer ${getToken()}` }
  })
  return data
}
