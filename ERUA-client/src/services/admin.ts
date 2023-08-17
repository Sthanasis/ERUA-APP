import { deleteRequest, get } from './base';

const baseUrl = '/admin';

const activateStakeholder = async (email: string) => {
  return await get(`${baseUrl}/authenticateStakeholder?email=${email}`);
};

const deactivateStakeholder = async (email: string) => {
  return await get(`${baseUrl}/deactivateStakeholder?email=${email}`);
};

const deleteSolution = async (id: number) => {
  return await deleteRequest(`${baseUrl}/solution?solutionId=${id}`);
};

const deleteProblem = async (id: number) => {
  return await deleteRequest(`${baseUrl}/problem?problemId=${id}`);
};

export const adminService = Object.freeze({
  activateStakeholder,
  deactivateStakeholder,
  deleteProblem,
  deleteSolution,
});
