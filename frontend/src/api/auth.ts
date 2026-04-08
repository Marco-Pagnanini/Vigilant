import axios from 'axios'

const BASE_URL = import.meta.env.VITE_API_BASE_URL ?? ''

export const login = async (username: string, password: string): Promise<string> => {
  const { data } = await axios.post(`${BASE_URL}/api/auth/login`, { username, password })
  return data.token
}

export const getToken = (): string | null => localStorage.getItem('vigilant_token')

export const saveToken = (token: string): void => localStorage.setItem('vigilant_token', token)

export const removeToken = (): void => localStorage.removeItem('vigilant_token')
